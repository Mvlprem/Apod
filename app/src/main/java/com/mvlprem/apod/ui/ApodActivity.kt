package com.mvlprem.apod.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mvlprem.apod.R
import com.mvlprem.apod.databinding.ActivityApodBinding
import com.mvlprem.apod.util.userPreferredTheme
import com.mvlprem.apod.viewmodels.SharedViewModel
import com.mvlprem.apod.viewmodels.ViewModelProvider

/**
 * This is a single activity application that uses the Navigation library.
 * Content is displayed by Fragments.
 */
class ApodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApodBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private val sharedViewModel: SharedViewModel by viewModels { ViewModelProvider(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Observing readFromDataStore and changing theme
         * by passing the value to function [userPreferredTheme] in Util file
         */
        sharedViewModel.readFromDataStore.observe(this, { userPreferredTheme(it) })

        binding = ActivityApodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Bottom navigation setup with [navController]
         */
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView = binding.bottomNavigation.apply {
            setupWithNavController(navController)
            setOnItemReselectedListener { }
        }
    }
}