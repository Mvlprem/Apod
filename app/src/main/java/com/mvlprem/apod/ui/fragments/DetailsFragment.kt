package com.mvlprem.apod.ui.fragments

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mvlprem.apod.R
import com.mvlprem.apod.databinding.FragmentDetailsBinding
import com.mvlprem.apod.domain.Pictures
import com.mvlprem.apod.util.toastMessage
import com.mvlprem.apod.viewmodels.SharedViewModel
import java.io.File

/**
 * Displays detailed information about a selected piece of [Pictures].
 * which it gets as a Parcelable property through Jetpack Navigation's SafeArgs.
 */
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var picture: Pictures
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_details, container, false)

        /**
         * Gets selected [Pictures] through Navigation's SafeArgs
         */
        picture = DetailsFragmentArgs.fromBundle(requireArguments()).picture

        /**
         * Passing safeargs [Pictures] information to [sharedViewModel]
         */
        sharedViewModel.fragmentArgsData(picture)
        binding.viewModel = sharedViewModel

        /**
         * onClick checks if [Pictures] has 'hdUrl'
         * if true navigates to [ImageFragment]
         * else calls an intent
         */
        binding.image.setOnClickListener {
            if (picture.hdurl != null) {
                findNavController().navigate(
                    DetailsFragmentDirections.actionDetailsFragmentToImageFragment()
                )
            } else {
                intent()
            }
        }

        /**
         * Share [Pictures] information as plain text onClick
         */
        binding.btnShare.setOnClickListener {
            val text = picture.title + "\n" + picture.url
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Nasa Apod")
            startActivity(shareIntent)
        }

        /**
         * Downloads picture onClick
         */
        binding.btnDownload.setOnClickListener {
            downloader(picture.title, picture.url)
        }

        /**
         * Add [Pictures] to favorites onClick,
         * displays a toast message
         */
        binding.btnSave.setOnClickListener {
            sharedViewModel.addFavorite(picture)
            toastMessage(requireContext(), R.string.successful)
        }

        return binding.root
    }

    /**
     * Intent to open [Pictures] 'url'
     */
    private fun intent() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(picture.url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            toastMessage(requireContext(), R.string.failed_intent)
        }
    }

    /**
     * Download picture into device storage by a
     * request that a URI be downloaded to a particular destination file
     * Saves file to 'Downloads' directory, creates one if directory doesn't exist
     * Displays a toast message on request
     * @param title picture title
     * @param url picture url
     */
    private fun downloader(title: String, url: String) {
        val directory = File(Environment.DIRECTORY_DOWNLOADS)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val downloadManager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri).apply {
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(directory.toString(), title)
            setTitle(title)
            setRequiresCharging(false)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }
        downloadManager.enqueue(request)
        toastMessage(requireContext(), R.string.downloading)
    }
}