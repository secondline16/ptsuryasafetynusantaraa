package com.ssn.app

import android.app.Application
import com.ssn.app.data.api.config.ApiClient
import com.ssn.app.data.preference.SharedPrefProvider
import com.ssn.app.helper.Helper
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initAppPreferences()
        initTimber()
        initApiClient()
        initPRDownloader()
    }

    private fun initPRDownloader() {
        // Setting timeout globally for the download network requests
//        val config = PRDownloaderConfig.newBuilder()
//            .setReadTimeout(30000)
//            .setConnectTimeout(30000)
//            .build()
//        PRDownloader.initialize(this, config)
    }

    private fun initApiClient() {
        ApiClient.init(this)
    }

    private fun initAppPreferences() {
        SharedPrefProvider.initAppPreferences(this)
    }

    private fun initTimber() {
        if (Helper.isDebugBuild()) {
            Timber.plant(Timber.DebugTree())
        }
    }

}