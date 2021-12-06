package com.mvlprem.apod.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mvlprem.apod.R
import com.mvlprem.apod.databinding.FragmentImageBinding
import com.mvlprem.apod.viewmodels.SharedViewModel

/**
 * Displays Picture
 */
class ImageFragment : Fragment() {

    private lateinit var binding: FragmentImageBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_image, container, false)
        binding.viewModel = sharedViewModel



        return binding.root
    }
}