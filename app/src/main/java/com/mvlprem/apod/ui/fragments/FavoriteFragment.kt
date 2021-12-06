package com.mvlprem.apod.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mvlprem.apod.R
import com.mvlprem.apod.adapters.ItemClickListener
import com.mvlprem.apod.adapters.PicturesAdapter
import com.mvlprem.apod.databinding.FragmentFavoriteBinding
import com.mvlprem.apod.viewmodels.SharedViewModel

/**
 * Displays list of Pictures
 */
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_favorite, container, false)
        binding.viewModel = sharedViewModel

        /**
         * Adapter for { recyclerview } with clickHandler lambda that
         * tells when our { picture } item is clicked and
         * navigates to a [DetailsFragment] with selected { picture } data as parcelable
         */
        val adapter = PicturesAdapter(ItemClickListener { picture ->
            findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(picture)
            )
        })
        /**
         * Sets the adapter of recyclerview
         */
        binding.favoriteRecyclerView.adapter = adapter
        /**
         * Observing { favoritePictures } and submitting List to adapter
         */
        sharedViewModel.favoritePictures.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.hint.visibility = View.GONE
                binding.favoriteRecyclerView.visibility = View.VISIBLE
                adapter.submitList(it)
            } else {
                binding.favoriteRecyclerView.visibility = View.GONE
                binding.hint.visibility = View.VISIBLE
            }

        })


        return binding.root
    }
}