package com.mvlprem.apod.network

import com.mvlprem.apod.database.DatabasePictures

/**
 * Data class with properties that match the JSON response fields.
 */
data class NetworkPictures(
    val date: String,
    val title: String,
    val explanation: String,
    val hdurl: String?,
    val url: String
)

/**
 * Convert Network results to database objects
 */
fun List<NetworkPictures>.asDatabaseModel(): Array<DatabasePictures> {
    return map {
        DatabasePictures(
            date = it.date,
            title = it.title,
            explanation = it.explanation,
            hdurl = it.hdurl,
            url = it.url
        )
    }.toTypedArray()
}