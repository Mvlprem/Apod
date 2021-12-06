package com.mvlprem.apod.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.mvlprem.apod.R
import com.mvlprem.apod.adapters.ItemClickListener
import com.mvlprem.apod.adapters.PicturesAdapter
import com.mvlprem.apod.databinding.FragmentSearchBinding
import com.mvlprem.apod.util.constraintsBuilder
import com.mvlprem.apod.util.millisToDate
import com.mvlprem.apod.viewmodels.SharedViewModel

/**
 * Displays list of Pictures
 */
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search, container, false)
        binding.viewModel = sharedViewModel

        /**
         * MaterialDatePicker Builder
         * Sets Calendar Constraints by calling [constraintsBuilder] util function
         */
        val datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setCalendarConstraints(constraintsBuilder())
            .build()

        /**
         * Displays MaterialDatePicker onClick
         */
        binding.btnFab.setOnClickListener {
            datePicker.show(requireActivity().supportFragmentManager, "tag")
        }

        /**
         * The contextView is used to make snackBar
         */
        val contextView = binding.searchRecyclerView

        /**
         * Observing the { networkResponse } and showing a snackBar if not true
         * If true Gets picture information from the API Retrofit service for SelectedDates
         */
        datePicker.addOnPositiveButtonClickListener {
            val startDate = millisToDate(it.first)
            val endDate = millisToDate(it.second)
            sharedViewModel.networkResponse.observe(viewLifecycleOwner, { value ->
                if (value != true) {
                    Snackbar.make(
                        contextView,
                        R.string.network_error,
                        Snackbar.LENGTH_SHORT
                    )
                        .setAnchorView(R.id.bottom_navigation)
                        .show()
                } else {
                    sharedViewModel.getSelectedDates(startDate, endDate)
                }
            })
        }

        /**
         * Adapter for { recyclerview } with clickHandler lambda that
         * tells when our { picture } item is clicked and
         * navigates to a [DetailsFragment] with selected { picture } data as parcelable
         */
        val adapter = PicturesAdapter(ItemClickListener { picture ->
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailsFragment(picture)
            )
        })
        /**
         * Sets the adapter of recyclerview
         */
        binding.searchRecyclerView.adapter = adapter

        /**
         * Observing { selectedDates } and submitting List to adapter
         */
        sharedViewModel.selectedDates.observe(viewLifecycleOwner, { list ->
            if (list.isNotEmpty()) {
                binding.hint.visibility = View.GONE
                binding.searchRecyclerView.visibility = View.VISIBLE
                adapter.submitList(list)
            } else {
                binding.searchRecyclerView.visibility = View.GONE
                binding.hint.visibility = View.VISIBLE
            }
        })

        return binding.root
    }
}