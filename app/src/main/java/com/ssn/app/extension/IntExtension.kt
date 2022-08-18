package com.ssn.app.extension

import java.text.NumberFormat
import java.util.Locale

fun Int?.orZero(): Int = this ?: 0

fun Int?.toBoolean(): Boolean = this == 1

fun Int.addZeroPrefixIfNeeded(): String = if (this > 9 || this < 0) this.toString() else "0$this"

fun Int?.toCurrencyFormat(
    locale: Locale = Locale("in", "ID")
): String {
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    numberFormat.minimumFractionDigits = 0
    return numberFormat.format(orZero().toDouble())
}
