package com.mvlprem.apod.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mvlprem.apod.R
import com.mvlprem.apod.databinding.FragmentSettingsBinding
import com.mvlprem.apod.util.toastMessage
import com.mvlprem.apod.viewmodels.SharedViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    /**
     * Default selected theme item
     */
    private var selectedItem = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_settings, container, false)
        binding.viewModel = sharedViewModel

        /**
         * Observing { readFromDataStore } and sets it to [selectedItem]
         */
        sharedViewModel.readFromDataStore.observe(viewLifecycleOwner, {
            selectedItem = it
        })

        /**
         * Displays dialogue onClick
         */
        binding.theme.setOnClickListener {
            materialDialogue()
        }

        /**
         * Displays alert dialog onClick
         */
        binding.deleteCache.setOnClickListener {
            alertDialogue(R.string.cache, R.string.cache_message)
                /**
                 * Deletes everything in offline cache and make a new api call
                 * stores the new data to offline cache
                 * displays a toast message
                 */
                .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                    sharedViewModel.apply {
                        deleteCache()
                        getPictures()
                    }
                    toastMessage(requireContext(), R.string.deleted)
                }
                .show()
        }

        /**
         * Displays alert dialog onClick
         */
        binding.deleteFavorites.setOnClickListener {
            alertDialogue(R.string.delete, R.string.delete_message)
                /**
                 * Deletes all user favorites and
                 * displays a toast message
                 */
                .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                    sharedViewModel.deleteFavorites()
                    toastMessage(requireContext(), R.string.deleted)
                }
                .show()
        }

        return binding.root
    }

    /**
     * When called displays dialogue with options to choose different theme
     * on selection calls { saveToDataStore } and stores the value
     */
    private fun materialDialogue() {
        val singleItem = arrayOf("Light", "Dark", "System default")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialogue_title)
            .setSingleChoiceItems(singleItem, selectedItem) { dialog, which ->
                val item: Int = when (which) {
                    0 -> which
                    1 -> which
                    else -> {
                        2
                    }
                }
                sharedViewModel.saveToDataStore(item)
                dialog.dismiss()
            }.show()
    }

    /**
     * when called Builds alert dialog with
     * @param title dialog title
     * @param message of action that we are about to perform
     * @return AlertDialog
     */
    private fun alertDialogue(title: Int, message: Int): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_DatePicker)
            .setTitle(resources.getString(title))
            .setMessage(resources.getString(message))
            .setNegativeButton(resources.getString(R.string.decline)) { dialog, _ ->
                dialog.dismiss()
            }
    }
}