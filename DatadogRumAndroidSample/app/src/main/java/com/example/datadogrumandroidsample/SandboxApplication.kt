package com.example.datadogrumandroidsample

import android.app.Application
import android.util.Log
import com.datadog.android.Datadog
import com.datadog.android.DatadogSite
import com.datadog.android.core.configuration.Configuration
import com.datadog.android.core.configuration.Credentials
import com.datadog.android.privacy.TrackingConsent
import com.datadog.android.rum.GlobalRum
import com.datadog.android.rum.RumMonitor
import com.datadog.android.rum.tracking.ActivityViewTrackingStrategy


class SandboxApplication : Application() {
	override fun onCreate() {

		super.onCreate()
		Log.i("rum_test", "oncreate")

		initRum()

		Log.i("rum_test", "init Complete")
	}

	private fun initRum() {
		val clientToken = "CLIENT_TOKEN_HERE"
		val applicationId = "RUM_APPLICATION_ID_HERE"
		val environmentName = "dev"
		val appVariantName = "DatadogRumAndroidSample"

		val configuration = Configuration.Builder(
			logsEnabled = true,
			tracesEnabled = true,
			crashReportsEnabled = true,
			rumEnabled = true
		)
			.trackInteractions()
			.trackLongTasks(100)
			.useViewTrackingStrategy(ActivityViewTrackingStrategy(true))
			.useSite(DatadogSite.US1)
			.build()
		val credentials = Credentials(clientToken, environmentName, appVariantName, applicationId)
		Datadog.initialize(this, credentials, configuration, TrackingConsent.GRANTED)
		//Sample rate
		GlobalRum.registerIfAbsent(RumMonitor.Builder().sampleRumSessions(100.0f).build())
	}
}
