package com.example.datadogrumandroidsample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.datadog.android.Datadog
import com.example.datadogrumandroidsample.databinding.ActivityAnotherBinding
import com.example.datadogrumandroidsample.databinding.ActivityMainBinding
import retrofit2.Retrofit

class AnotherActivity: AppCompatActivity()
{	private lateinit var binding: ActivityAnotherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnotherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMainActivityButtonView()
    }

    private fun initMainActivityButtonView() {
        binding.returnToMain.setOnClickListener {
            finish();

        }
    }
}
