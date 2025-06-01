package com.mypills.core.utils

import kotlinx.datetime.*
import java.text.NumberFormat
import java.util.*

// Date extensions
fun LocalDate.toDisplayString(): String {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return when {
        this == today -> "Hoje"
        this == today.minus(DatePeriod(days = 1)) -> "Ontem"
        this == today.plus(DatePeriod(days = 1)) -> "Amanhã"
        else -> "$dayOfMonth/${monthNumber}/${year}"
    }
}

fun Instant.toRelativeTimeString(): String {
    val now = Clock.System.now()
    val diffMillis = toEpochMilliseconds() - now.toEpochMilliseconds()
    val diffMinutes = (diffMillis / 60000).toInt()
    
    return when {
        diffMinutes < -60 -> "Há ${-diffMinutes / 60}h"
        diffMinutes < 0 -> "Há ${-diffMinutes}min"
        diffMinutes == 0 -> "Agora"
        diffMinutes < 60 -> "Em ${diffMinutes}min"
        diffMinutes < 1440 -> "Em ${diffMinutes / 60}h"
        else -> "Em ${diffMinutes / 1440}d"
    }
}

// Currency extensions
fun Double.toCurrencyString(currency: String = "BRL"): String {
    val locale = if (currency == "BRL") Locale("pt", "BR") else Locale.getDefault()
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(this)
}

// String extensions
fun String.toSlug(): String = this
    .lowercase()
    .replace(Regex("[^a-z0-9\\s]"), "")
    .replace(Regex("\\s+"), "-")
    .trim('-')

fun String.isValidEmail(): Boolean = 
    android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPin(): Boolean = 
    matches(Regex("^\\d{4}$"))

// Collection extensions
fun <T> List<T>.safeGet(index: Int): T? = 
    if (index in indices) get(index) else null

inline fun <T> List<T>.sumOfNotNull(selector: (T) -> Double?): Double =
    mapNotNull(selector).sum()
