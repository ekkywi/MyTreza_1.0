package com.trezanix.mytreza.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrencyDisplay(value: String, code: String): String {
    if (value.isBlank()) return "$code 0"

    return when (code) {
        "IDR" -> {
            // val number = value.toBigDecimalOrNull() ?: BigDecimal.ZERO
            // val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            // format.format(number)

            "Rp $value"
        }
        "USD" -> "$ $value"
        "EUR" -> "€ $value"
        "JPY" -> "¥ $value"
        "BTC" -> "$value BTC"
        "ETH" -> "$value ETH"
        else -> "$value $code"
    }
}