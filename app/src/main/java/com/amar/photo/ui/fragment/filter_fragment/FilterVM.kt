package com.amar.photo.ui.fragment.filter_fragment

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amar.photo.data_state.DataState
import com.amar.photo.utils.imgGallery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterVM @Inject constructor(private val filterRP: FilterRP): ViewModel() {

    init {
        imgGallery?.let { bitmap ->
            loadingImageFilters(bitmap)
        }
    }


    // REGION:: Prepare Image Preview
    private val imagePreviewDataState = MutableLiveData<DataState<Bitmap>>()
    val imagePreviewUiState: MutableLiveData<DataState<Bitmap>> get() = imagePreviewDataState
    private fun emitImagePreviewUiState(isLoading: Boolean = false, bitmap: Bitmap? = null, error: String? = null) {
        val dataState = DataState(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dataState)
    }
    fun prepareImagePreview(imageUri: Uri) {
        viewModelScope.launch (Dispatchers.IO) {
            runCatching {
                emitImagePreviewUiState(isLoading = true)
                filterRP.prepareImagePreview(imageUri)
            }.onSuccess {  bitmap ->
                if (bitmap != null) {
                    emitImagePreviewUiState(bitmap = bitmap)
                } else {
                    emitImagePreviewUiState(error = "Unable to image preview")
                }
            }.onFailure {  error ->
                emitImagePreviewUiState(error = error.toString())
            }
        }
    }

    // REGION: Load Image Filter
    private val imageFiltersDataState = MutableLiveData<DataState<ArrayList<Filter>>>()
    val imageFiltersUiState : MutableLiveData<DataState<ArrayList<Filter>>> get() = imageFiltersDataState
    private fun emitImageFiltersUiState (isLoading: Boolean = false, data: ArrayList<Filter>? = null, error: String? = null) {
        val dataState = DataState(isLoading, data, error)
        imageFiltersDataState.postValue(dataState)
    }
    private fun loadingImageFilters(originalBitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                emitImageFiltersUiState(isLoading = true)
                filterRP.getImageFilters(originalBitmap)
            }.onSuccess { list ->
                if (list.isEmpty()){
                    emitImageFiltersUiState(error = "Can't load filters")
                } else {
                    emitImageFiltersUiState(data = list)
                }
            }.onFailure { error ->
                emitImageFiltersUiState(error = error.toString())
            }
        }
    }


}