package com.mvlprem.apod.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory for constructing [SharedViewModel] with parameter
 */
class ViewModelProvider(private val application: Application) :
    ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(application) as T
        }

        throw IllegalArgumentException("Failed to provide ViewModel")
    }
}