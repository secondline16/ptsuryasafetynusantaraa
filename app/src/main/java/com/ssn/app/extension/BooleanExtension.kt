package com.ssn.app.extension

fun Boolean?.toInt(): Int = if (this == true) 1 else 0

fun Boolean?.orFalse(): Boolean = this ?: false

fun Boolean?.orTrue(): Boolean = this ?: true