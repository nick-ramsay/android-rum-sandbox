package com.example.datadogrumandroidsample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.datadog.android.Datadog
import com.datadog.android.DatadogEventListener
import com.datadog.android.DatadogInterceptor
import com.datadog.android.rum.GlobalRum
import com.example.datadogrumandroidsample.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	private var mRetrofit: Retrofit? = null

	private var mRetrofitAPI: RetrofitAPI? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		initRequestButtonView()
		initTimingButtonView()
		initCrashButtonView()
		initNewActivityButtonView()
		//SetUser
		Datadog.setUserInfo("1", "User1", "user1@datadoghq.com")
		initRetrofitInit()
	}

	private fun initRequestButtonView() {
		binding.request.setOnClickListener {
			val testString = mRetrofitAPI?.test()
			testString?.enqueue(object : Callback<JsonPlaceholderDataResponse> {
				override fun onResponse(call: Call<JsonPlaceholderDataResponse>, response: Response<JsonPlaceholderDataResponse>) {
					Log.i("rum_test", "responseHeaders" + response.headers())
					Log.i("rum_test", "responseBody" + response.body())
				}

				override fun onFailure(call: Call<JsonPlaceholderDataResponse>, t: Throwable) {
					Log.i("rum_test", "Request Failed:" + t)
				}
			})
		}
	}

	private fun initTimingButtonView() {
		binding.timing.setOnClickListener {
			// Timing
			GlobalRum.get().addTiming("initButton_load_timing")
			// Global Context
			GlobalRum.addAttribute("paid", "2")
		}
	}
	private fun initCrashButtonView() {
		binding.crash.setOnClickListener {
			throw RuntimeException("Test Crash"); // Force a crash

		}
	}
	private fun initNewActivityButtonView() {
		binding.newFragment.setOnClickListener {
			val intent = Intent(this, AnotherActivity::class.java)
			// start your next activity
			startActivity(intent)
		}
	}
	fun initRetrofitInit() {
		val okHttpClient = OkHttpClient.Builder()
			.addInterceptor(DatadogInterceptor(rumResourceAttributesProvider = CustomRumResourceAttributesProvider()))
			.eventListenerFactory(DatadogEventListener.Factory())
			.build()

		mRetrofit =  Retrofit.Builder()
			.client(okHttpClient)
			.baseUrl("https://jsonplaceholder.typicode.com/")
			.addConverterFactory(GsonConverterFactory.create())
			.build();

		mRetrofitAPI = mRetrofit?.create(RetrofitAPI::class.java)
	}

	override fun onResume() {
		super.onResume()
		//onResume 시 로그 전송
		GlobalRum.get().startView(this, "MainActivity", HashMap<String, String>().apply { put("onResume", "onResuemValue") })
	}

	override fun onPause() {
		super.onPause()
		//onPause 시 로그 전송
		GlobalRum.get().stopView(this, HashMap<String, String>().apply { put("onPause", "onPauseValue") })
	}

}