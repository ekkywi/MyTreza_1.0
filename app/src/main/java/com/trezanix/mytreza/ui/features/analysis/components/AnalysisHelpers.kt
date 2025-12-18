package com.trezanix.mytreza.ui.features.analysis.components

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    format.maximumFractionDigits = 0
    return format.format(amount).replace("Rp", "Rp ")
}

fun formatCurrencyCompact(amount: Double): String {
    return if (amount >= 1000000) {
        val millions = amount / 1000000
        val format = NumberFormat.getInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 1
        "Rp ${format.format(millions)}jt"
    } else {
        formatCurrency(amount)
    }
}