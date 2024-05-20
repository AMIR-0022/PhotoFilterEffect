package com.amar.photo.ui.fragment.folder_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amar.photo.R
import com.amar.photo.databinding.FragmentGalleryFolderBinding
import com.amar.photo.ui.activity.gallery_activity.GalleryVM
import com.amar.photo.utils.displayToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFolderFragment : Fragment() {

    private lateinit var binding: FragmentGalleryFolderBinding

    private val viewModel: GalleryVM by viewModels()

    private lateinit var galleryFolderAdapter: GalleryFolderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_gallery_folder, container, false)
        binding = FragmentGalleryFolderBinding.bind(myView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        populateData()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.layoutHeader.ivBack.setOnClickListener {
            findNavController().popBackStack()
            requireActivity().finish()
        }
    }

    private fun populateData() {
        // --->> set GalleryFolder Adapter-Data
        galleryFolderAdapter = GalleryFolderAdapter {folder ->
            navigateToImageFragment(folder)
        }
        binding.rvGalleryFolder.adapter = galleryFolderAdapter
        // --->>> observe gallery folder data
        viewModel.galleryFolderUiState.observe(viewLifecycleOwner) {
            val galleryFolderDataState = it ?: return@observe

            binding.pbGalleryFolder.visibility =
                if (galleryFolderDataState.isLoading) View.VISIBLE else View.GONE

            galleryFolderDataState.data?.let { data ->
                if (data.isEmpty()) {
                    binding.tvEmptyGallery.visibility = View.VISIBLE
                } else {
                    galleryFolderAdapter.setData(data)
                }
            } ?: run {
                galleryFolderDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }

    }

    private fun navigateToImageFragment(folder: GalleryFolder) {
        if (folder.folderName != null) {
            val action = GalleryFolderFragmentDirections
                .actionGalleryFolderFragmentToGalleryImageFragment(folderName = "${folder.folderName}")
            findNavController().navigate(action)
        }
    }


}