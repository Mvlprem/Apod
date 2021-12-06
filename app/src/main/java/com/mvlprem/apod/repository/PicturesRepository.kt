package com.mvlprem.apod.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mvlprem.apod.BuildConfig
import com.mvlprem.apod.database.FavoritePictures
import com.mvlprem.apod.database.PicturesDatabase
import com.mvlprem.apod.database.asDomainModel
import com.mvlprem.apod.database.domainModel
import com.mvlprem.apod.domain.Pictures
import com.mvlprem.apod.network.NetworkPictures
import com.mvlprem.apod.network.Service
import com.mvlprem.apod.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PicturesRepository(private val database: PicturesDatabase) {

    /**
     * A list of pictures {DatabasePictures} that can be shown on the screen.
     * Using Transformations to convert to domain object by calling extension fun [asDomainModel]
     */
    val pictures: LiveData<List<Pictures>> =
        Transformations.map(database.picturesDao.getPictures()) {
            it.asDomainModel()
        }

    /**
     * A list of pictures {FavoritePictures} that can be shown on the screen.
     * Using Transformations to convert to domain object by calling extension fun [domainModel]
     */
    val favoritePictures: LiveData<List<Pictures>> =
        Transformations.map(database.picturesDao.getFavorites()) {
            it.domainModel()
        }

    /**
     * Deletes everything from table {DatabasePictures}
     */
    suspend fun deleteCache() {
        withContext(Dispatchers.IO) {
            database.picturesDao.deleteCache()
        }
    }

    /**
     * Deletes everything from table {FavoritePictures}
     */
    suspend fun deleteFavorites() {
        withContext(Dispatchers.IO) {
            database.picturesDao.deleteFavorites()
        }
    }

    /**
     * Insert new picture into table [FavoritePictures]
     * @param pictures information of picture to be saved
     */
    suspend fun addToFavorites(pictures: Pictures) {
        withContext(Dispatchers.IO) {
            database.picturesDao.insertFavorite(
                FavoritePictures(
                    date = pictures.date,
                    title = pictures.title,
                    explanation = pictures.explanation,
                    hdurl = pictures.hdurl,
                    url = pictures.url
                )
            )
        }
    }

    /**
     * Gets picture information from the API Retrofit service and
     * refresh the pictures stored in the offline cache.
     * @param startDate date of picture posted
     * @param endDate date of picture posted
     */
    suspend fun refreshPictures(startDate: String, endDate: String?) {
        var response: List<NetworkPictures>
        withContext(Dispatchers.IO) {
            response = try {
                Service.api.getPictures(BuildConfig.apiKey, startDate, endDate)
            } catch (e: HttpException) {
                ArrayList()
            }
            database.picturesDao.insertAll(*response.asDatabaseModel())
        }
    }

    /**
     * Gets picture information of selected dates using API Retrofit service
     * @param startDate date of picture posted
     * @param endDate date of picture posted
     * @return domain object { List<Pictures> }
     */
    suspend fun getSelectedDates(startDate: String, endDate: String?): List<Pictures> {
        var response: List<Pictures>
        withContext(Dispatchers.IO) {
            response = try {
                Service.api.getSelectedDates(BuildConfig.apiKey, startDate, endDate)
            } catch (e: HttpException) {
                ArrayList()
            }
        }
        return response
    }
}