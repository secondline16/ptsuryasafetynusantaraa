package com.ssn.app.ui.splash

import androidx.lifecycle.ViewModel
import com.ssn.app.data.preference.SharedPrefProvider

class SplashViewModel : ViewModel() {

    fun isLoggedIn(): Boolean = SharedPrefProvider.getIsLogin()

}