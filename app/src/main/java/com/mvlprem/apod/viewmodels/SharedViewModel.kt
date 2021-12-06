package com.mvlprem.apod.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.mvlprem.apod.ApodApplication
import com.mvlprem.apod.database.DataStore
import com.mvlprem.apod.database.PicturesDatabase
import com.mvlprem.apod.domain.Pictures
import com.mvlprem.apod.repository.PicturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class SharedViewModel(application: Application) :
    AndroidViewModel(application) {

    /**
     * Creating DataStore by passing { application } as context
     */
    private val datastore = DataStore(application)

    /**
     * Gets the value stored in [DataStore] in the form of Flow<Int> and
     * converting as [LiveData] by calling { asLiveData() } on it so it
     * can be observed.
     */
    val readFromDataStore = datastore.read.asLiveData()

    /**
     * Saves user selected theme in DataStore by calling [DataStore.save]
     * since it's a suspend function it needs to be called in coroutine scope
     * @param value selected theme
     */
    fun saveToDataStore(value: Int) = viewModelScope.launch(Dispatchers.IO) {
        datastore.save(value)
    }

    /**
     * Instantiating [PicturesDatabase]
     */
    private val database = PicturesDatabase.getDatabase(application)

    /**
     * providing [database] to [PicturesRepository]
     */
    private val picturesRepository = PicturesRepository(database)

    /**
     * stores the [Pictures] [List] from { DetailFragmentArgs }
     * MutableLiveData, because we will be updating the List with new values
     */
    private val _parcelizeData = MutableLiveData<Pictures>()
    val parcelizeData: LiveData<Pictures> = _parcelizeData

    /**
     * stores the [Pictures] [List] from retrofit service
     * MutableLiveData, because we will be updating the List with new values
     */
    private val _selectedDates = MutableLiveData<List<Pictures>>()
    val selectedDates: LiveData<List<Pictures>> = _selectedDates

    /**
     * stores the status of user network connectivity
     */
    private val _networkResponse = MutableLiveData<Boolean>()
    val networkResponse: LiveData<Boolean> = _networkResponse

    private val date = LocalDate.now().minusDays(3).toString()

    /**
     * Gets picture information [LiveData] stored in offline cache
     */
    val picture = picturesRepository.pictures

    /**
     * Gets picture information [LiveData] stored in favorites
     */
    val favoritePictures = picturesRepository.favoritePictures

    /**
     * Calling { getPictures() } on init to update offline cache.
     */
    init {
        getPictures()
    }

    /**
     * Gets picture information from the API Retrofit service
     * calling { refreshPictures } from [picturesRepository]
     * updates the [_networkResponse] [Boolean]
     */
    fun getPictures() {
        if (isNetworkConnected()) {
            viewModelScope.launch {
                picturesRepository.refreshPictures(date, null)
                _networkResponse.value = true
            }
        } else {
            _networkResponse.value = false
        }
    }

    /**
     * Gets filtered [Pictures] information from the API Retrofit service
     * updates the [_networkResponse] [Boolean] and [_selectedDates] [List]
     * @param startDate date of picture posted
     * @param endDate date of picture posted
     */
    fun getSelectedDates(startDate: String, endDate: String) {
        if (isNetworkConnected()) {
            viewModelScope.launch {
                _selectedDates.value = picturesRepository.getSelectedDates(startDate, endDate)
                _networkResponse.value = true
            }
        } else {
            _networkResponse.value = false
        }
    }

    /**
     * Stores new picture to favorites by calling { addToFavorites } function
     * from [PicturesRepository]
     * @param pictures user selected picture
     */
    fun addFavorite(pictures: Pictures) {
        viewModelScope.launch {
            picturesRepository.addToFavorites(pictures)
        }
    }

    /**
     * Gets information from the { DetailFragmentArgs } and
     * updates the [_parcelizeData] [List]
     * @param pictures that has been clicked in recycler list.
     */
    fun fragmentArgsData(pictures: Pictures) {
        _parcelizeData.value = pictures
    }

    /**
     * Deletes offline cache by calling [deleteCache] function
     * from [PicturesRepository]
     */
    fun deleteCache() {
        viewModelScope.launch {
            picturesRepository.deleteCache()
        }
    }

    /**
     * Deletes all favorites by calling [deleteFavorites] function
     * from [PicturesRepository]
     */
    fun deleteFavorites() {
        viewModelScope.launch {
            picturesRepository.deleteFavorites()
        }
    }

    /**
     * Checks user's network connectivity when called
     * need ACCESS_NETWORK_STATE permission in Manifest
     * @return Boolean
     */
    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getApplication<ApodApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
    }
}