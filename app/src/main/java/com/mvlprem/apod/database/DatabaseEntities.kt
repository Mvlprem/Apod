package com.mvlprem.apod.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvlprem.apod.domain.Pictures

/**
 * Data class with properties that match the JSON response fields
 * These properties will be our columns and the class is our table.
 * This table is used for offline caching.
 */
@Entity
data class DatabasePictures(
    @PrimaryKey
    val date: String,
    val title: String,
    val explanation: String,
    val hdurl: String?,
    val url: String
)

/**
 * Convert [DatabasePictures] results to Domain objects
 */
fun List<DatabasePictures>.asDomainModel(): List<Pictures> {
    return map {
        Pictures(
            date = it.date,
            title = it.title,
            explanation = it.explanation,
            hdurl = it.hdurl,
            url = it.url
        )
    }
}

/**
 * Data class with properties that match the JSON response fields
 * These properties will be our columns and the class is our table.
 * This table is used to store user favorites
 */
@Entity
data class FavoritePictures(
    @PrimaryKey
    val date: String,
    val title: String,
    val explanation: String,
    val hdurl: String?,
    val url: String
)

/**
 * Convert [FavoritePictures] results to Domain objects
 */
fun List<FavoritePictures>.domainModel(): List<Pictures> {
    return map {
        Pictures(
            date = it.date,
            title = it.title,
            explanation = it.explanation,
            hdurl = it.hdurl,
            url = it.url
        )
    }
}