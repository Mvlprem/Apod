package com.mvlprem.apod.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class with properties that match the JSON response fields
 * It's Parcelize to share between fragments
 */
@Parcelize
data class Pictures(
    val date: String,
    val title: String,
    val explanation: String,
    val hdurl: String?,
    val url: String
) : Parcelable