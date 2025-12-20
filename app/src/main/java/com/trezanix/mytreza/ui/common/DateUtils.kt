package com.trezanix.mytreza.ui.common

import java.util.Calendar
import com.trezanix.mytreza.R

fun getGreetingResourceId() : Int {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 5..10 -> R.string.dashboard_greeting_morning
        in 11..14 -> R.string.dashboard_greeting_afternoon
        in 15..18 -> R.string.dashboard_greeting_evening
        else -> R.string.dashboard_greeting_night
    }
}