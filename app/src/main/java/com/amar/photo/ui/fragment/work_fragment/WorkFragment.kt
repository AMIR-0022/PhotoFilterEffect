package com.amar.photo.ui.fragment.work_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.amar.photo.R
import com.amar.photo.utils.AppConstants.FOLDER_NAME
import com.amar.photo.databinding.FragmentWorkBinding
import com.amar.photo.ui.activity.gallery_activity.GalleryVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkFragment : Fragment() {

    private lateinit var binding: FragmentWorkBinding

    private val viewModel: GalleryVM by viewModels()

    private lateinit var workAdapter: WorkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_work, container, false)
        binding = FragmentWorkBinding.bind(myView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        viewModel.loadGalleryImages(FOLDER_NAME)
    }

    private fun init() {
        populateData()
        setClickListeners()
    }

    private fun setClickListeners() {

    }

    private fun populateData() {
        // --->>> set Work-Image adapter data
        workAdapter = WorkAdapter { work ->

        }
        binding.rvWorkImages.adapter = workAdapter
        // observed worked image data
        viewModel.galleryImageUiState.observe(viewLifecycleOwner) {
            val dataState = it ?: return@observe

            binding.pbGalleryImages.visibility =
                if(dataState.isLoading) View.VISIBLE else View.GONE

            dataState.data?.let { data ->
                workAdapter.setData(data)
            } ?: run {
                dataState.error?.let { error ->
                    binding.tvEmptyWork.visibility = View.VISIBLE
                    //displayToast(error)
                }
            }
        }
    }

}