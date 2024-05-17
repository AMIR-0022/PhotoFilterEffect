package com.amar.photo.ui.home_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.amar.photo.R
import com.amar.photo.databinding.FragmentHomeBinding
import com.amar.photo.ui.home_fragment.category.CategoryAdapter
import com.amar.photo.ui.home_fragment.thumb.ThumbAdapter
import com.amar.photo.utils.displayToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

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
            Toast.makeText(requireContext(), "${thumb.blend}", Toast.LENGTH_SHORT).show()
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


}