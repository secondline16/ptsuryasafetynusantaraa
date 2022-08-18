package com.ssn.app.extension

import java.text.NumberFormat
import java.util.Locale

fun String?.orDash(): String = if (isNullOrEmpty()) "-" else this