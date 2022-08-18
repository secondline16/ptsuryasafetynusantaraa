package com.ssn.app.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ssn.app.ui.intro.IntroActivity
import com.ssn.app.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)
        initSplashScreenDelay()
    }

    private fun initSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition { true }
        }
    }

    private fun initSplashScreenDelay() {
        Handler(mainLooper).postDelayed(SPLASH_SCREEN_DELAY) {
            handleRedirection()
        }
    }

    private fun handleRedirection() {
        if (viewModel.isLoggedIn()) {
            navigateToMain()
        } else {
            navigateToIntro()
        }
    }

    private fun navigateToIntro() {
        IntroActivity.start(this)
    }

    private fun navigateToMain() {
        MainActivity.start(this)
    }

    companion object {
        private const val SPLASH_SCREEN_DELAY = 2000L
    }
}
