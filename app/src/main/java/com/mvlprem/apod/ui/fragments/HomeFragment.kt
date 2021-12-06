package com.mvlprem.apod.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mvlprem.apod.R
import com.mvlprem.apod.adapters.ItemClickListener
import com.mvlprem.apod.adapters.PicturesAdapter
import com.mvlprem.apod.databinding.FragmentHomeBinding
import com.mvlprem.apod.viewmodels.SharedViewModel

/**
 * Displays list of Pictures
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        binding.viewModel = sharedViewModel

        /**
         * Adapter for { recyclerview } with clickHandler lambda that
         * tells when our { picture } item is clicked and
         * navigates to a [DetailsFragment] with selected { picture } data as parcelable
         */
        val adapter = PicturesAdapter(ItemClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            )
        })
        /**
         * Sets the adapter of recyclerview
         */
        binding.homeRecyclerView.adapter = adapter
        /**
         * Observing { pictures } and submitting List to adapter
         */
        sharedViewModel.picture.observe(viewLifecycleOwner, {
            adapter.submitList(it.reversed())
        })

        /**
         * The contextView is used to make snackBar
         */
        val contextView = binding.homeRecyclerView

        /**
         * Observing the { networkResponse } and
         * showing a snackBar if not true
         */
        sharedViewModel.networkResponse.observe(viewLifecycleOwner, {
            if (it != true) {
                Snackbar.make(
                    contextView,
                    R.string.network_error,
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("RETRY") { sharedViewModel.getPictures() }
                    .setAnchorView(R.id.bottom_navigation)
                    .show()
            }
        })

        return binding.root
    }
}