package com.ssn.app.extension

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import com.ssn.app.helper.Helper

fun <T> Context.openActivity(
    destination: Class<T>,
    clearTask: Boolean = false,
    singleTop: Boolean = false,
    extras: Intent.() -> Unit = {}
) {
    val intent = Intent(this, destination)
    extras.invoke(intent)
    if (clearTask) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    if (singleTop) intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    startActivity(intent)
}

fun Context.isUsingNightMode(): Boolean = if (Helper.isAndroidROrHigher()) {
    resources.configuration.isNightModeActive
} else {
    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}
