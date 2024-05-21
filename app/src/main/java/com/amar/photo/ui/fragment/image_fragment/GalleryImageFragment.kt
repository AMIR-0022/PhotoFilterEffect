package com.amar.photo.ui.fragment.image_fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amar.photo.R
import com.amar.photo.databinding.FragmentGalleryImageBinding
import com.amar.photo.ui.activity.editor_activity.EditorActivity
import com.amar.photo.ui.activity.gallery_activity.GalleryVM
import com.amar.photo.utils.displayToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryImageFragment : Fragment() {

    private lateinit var binding: FragmentGalleryImageBinding

    private val viewModel: GalleryVM by viewModels()

    private lateinit var galleryImageAdapter: GalleryImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_gallery_image, container, false)
        binding = FragmentGalleryImageBinding.bind(myView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        val args: GalleryImageFragmentArgs by navArgs()
        val name: String = args.folderName
        viewModel.loadGalleryImages(name)
    }

    private fun init() {
        populateData()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.layoutHeader.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun populateData() {
        // --->>> set GalleryImage Adapter-Data
        galleryImageAdapter = GalleryImageAdapter {image ->
            startActivity(Intent(requireActivity(), EditorActivity::class.java))
        }
        binding.rvGalleryImages.adapter = galleryImageAdapter
        // --->>> observe gallery image data
        viewModel.galleryImageUiState.observe(viewLifecycleOwner) {
            val dataState = it ?: return@observe

            binding.pbGalleryImages.visibility =
                if(dataState.isLoading) View.VISIBLE else View.GONE

            dataState.data?.let { data ->
                galleryImageAdapter.setData(data)
            } ?: run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }

        }

    }

}