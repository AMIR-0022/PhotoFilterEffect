package com.amar.photo.ui.fragment.home_fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.amar.photo.R
import com.amar.photo.databinding.FragmentHomeBinding
import com.amar.photo.ui.activity.gallery_activity.ImageGalleryActivity
import com.amar.photo.ui.fragment.home_fragment.category.CategoryAdapter
import com.amar.photo.ui.fragment.home_fragment.thumb.Thumb
import com.amar.photo.ui.fragment.home_fragment.thumb.ThumbAdapter
import com.amar.photo.utils.PermissionsUtils
import com.amar.photo.utils.displayToast
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeVM by viewModels()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var thumbAdapter: ThumbAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(myView)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {
        populateData()
    }

    private fun populateData() {
        // --->>> set category adapter-data
        categoryAdapter = CategoryAdapter {item ->
            viewModel.activateCategory(item.id)
            viewModel.loadThumbImages(item.id)
        }
        binding.rvCategory.adapter = categoryAdapter
        // --->>> observe category data
        viewModel.categoryUiState.observe(viewLifecycleOwner) {
            val categoryDataState = it ?: return@observe

            binding.pbThumbImages.visibility =
                if (categoryDataState.isLoading) View.VISIBLE else View.GONE

            categoryDataState.data?.let { data ->
                categoryAdapter.setData(data)
            } ?: run {
                categoryDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }

        // --->>> set thumb adapter-data
        thumbAdapter = ThumbAdapter {position, thumb ->
            onThumbClick(position, thumb)
        }
        binding.rvThumb.adapter = thumbAdapter
        // --->>> observer thumb data
        viewModel.thumbImageUiState.observe(viewLifecycleOwner) {
            val thumbImagesDataState = it ?: return@observe

            binding.pbThumbImages.visibility =
                if (thumbImagesDataState.isLoading) View.VISIBLE else View.GONE

            thumbImagesDataState.data?.let { data ->
                thumbAdapter.setData(data)
            } ?: run {
                thumbImagesDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun onThumbClick(position: Int, thumb: Thumb){
        if (PermissionsUtils.hasPermission(requireContext())) {
            startActivity(Intent(requireActivity(), ImageGalleryActivity::class.java))
        } else {
            PermissionsUtils.requestPermission(requireActivity())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(p0: Int, p1: MutableList<String>) {}

    override fun onPermissionsDenied(p0: Int, p1: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, p1)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            PermissionsUtils.requestPermission(requireActivity())
        }
    }


}