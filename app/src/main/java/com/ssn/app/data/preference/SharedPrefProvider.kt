package com.ssn.app.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.ssn.app.model.User

object SharedPrefProvider {

    private const val APP_PREFERENCE = "app_preference"

    private lateinit var appPreferences: SharedPreferences

    fun initAppPreferences(context: Context) {
        if (::appPreferences.isInitialized) return
        appPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE)
    }

    fun getIsLogin(): Boolean = appPreferences.getBoolean(SharedPrefKey.SESSION_IS_LOGIN, false)

    fun setIsLogin(isLogin: Boolean) {
        appPreferences.edit {
            putBoolean(SharedPrefKey.SESSION_IS_LOGIN, isLogin)
        }
    }

    fun saveUser(user: User) {
        appPreferences.edit {
            putInt(SharedPrefKey.SESSION_USER_ID, user.id)
            putString(SharedPrefKey.SESSION_USERNAME, user.username)
            putString(SharedPrefKey.SESSION_USER_FULL_NAME, user.fullName)
            putString(SharedPrefKey.SESSION_USER_EMAIL, user.email)
            putString(SharedPrefKey.SESSION_USER_ADDRESS, user.address)
            putString(SharedPrefKey.SESSION_USER_PHOTO, user.photo)
            putString(SharedPrefKey.SESSION_USER_PHONE, user.phone)
            putString(SharedPrefKey.SESSION_USER_TOKEN, user.bearerToken)
        }
    }

    fun getUser(): User = with(appPreferences) {
        User(
            id = getInt(SharedPrefKey.SESSION_USER_ID, 0),
            username = getString(SharedPrefKey.SESSION_USERNAME, "").orEmpty(),
            fullName = getString(SharedPrefKey.SESSION_USER_FULL_NAME, "").orEmpty(),
            email = getString(SharedPrefKey.SESSION_USER_EMAIL, "").orEmpty(),
            address = getString(SharedPrefKey.SESSION_USER_ADDRESS, "").orEmpty(),
            photo = getString(SharedPrefKey.SESSION_USER_PHOTO, "").orEmpty(),
            phone = getString(SharedPrefKey.SESSION_USER_PHONE, "").orEmpty(),
            bearerToken = getString(SharedPrefKey.SESSION_USER_TOKEN, "").orEmpty()
        )
    }

    fun clearAppPref() {
        SharedPrefKey.all().forEach { key ->
            appPreferences.edit { remove(key) }
        }
    }
}
