package com.mvlprem.apod.util

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.datepicker.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Helper function to format date
 * @param [date] of picture posted
 * @return formatted date [String]
 */
fun dateFormatter(date: String): String {
    val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = LocalDate.parse(date, pattern)
    val dayOfWeek =
        formattedDate.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    val month = formattedDate.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    val dayOfMonth = formattedDate.dayOfMonth
    return "$dayOfWeek, $month $dayOfMonth"
}

/**
 * Helper function to change theme
 * @param value user selected theme
 */
fun userPreferredTheme(value: Int) {
    AppCompatDelegate.setDefaultNightMode(
        when (value) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO
            1 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
    )
}

/**
 * Helper function to provide CalendarConstraints to Date picker
 * Defines the start and end dates in date picker
 * Validators restricts date selection before start, after end date.
 */
fun constraintsBuilder(): CalendarConstraints {
    val today = MaterialDatePicker.todayInUtcMilliseconds()
    val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    calendar[Calendar.DAY_OF_MONTH] = 15
    calendar[Calendar.MONTH] = Calendar.JUNE
    calendar[Calendar.YEAR] = 1995
    val startAtThisYear = calendar.timeInMillis

    val validators: ArrayList<CalendarConstraints.DateValidator> = ArrayList()
    validators.add(DateValidatorPointForward.from(startAtThisYear))
    validators.add(DateValidatorPointBackward.now())

    return CalendarConstraints.Builder()
        .setStart(startAtThisYear)
        .setEnd(today)
        .setValidator(CompositeDateValidator.allOf(validators))
        .build()
}

/**
 * Helper function to convert millis [Long] to date [String]
 * @param millis date in milli seconds
 */
fun millisToDate(millis: Long): String {
    val pattern = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return pattern.format(millis)
}

/**
 * Helper function to make Toast
 * @param context required to create toast
 * @param message [String] res to display
 */
fun toastMessage(context: Context, message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}