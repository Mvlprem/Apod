package com.mvlprem.apod.network

import com.mvlprem.apod.domain.Pictures
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  A Retrofit service to fetch a list of Apod pictures.
 */
interface ApodService {
    @GET("apod")
    suspend fun getPictures(
        @Query("api_key") apikey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String?
    ): List<NetworkPictures>

    @GET("apod")
    suspend fun getSelectedDates(
        @Query("api_key") apikey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String?
    ): List<Pictures>
}

/**
 * Main entry point for network access. Call like `Service.api.getPictures()`
 */
object Service {

    /**
     * Build the Moshi object that Retrofit will be using,
     * Make sure to add the Kotlin adapter for full Kotlin compatibility.
     */
    private val moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * Configure retrofit to parse JSON
     */
    private val retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.nasa.gov/planetary/")
            .build()
    }

    /**
     * Generates an implementation of the [ApodService] interface.
     */
    val api: ApodService by lazy {
        retrofit.create(ApodService::class.java)
    }
}