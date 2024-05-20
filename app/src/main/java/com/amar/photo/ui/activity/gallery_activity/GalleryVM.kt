package com.amar.photo.ui.activity.gallery_activity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amar.photo.state.DataState
import com.amar.photo.ui.fragment.folder_fragment.GalleryFolder
import com.amar.photo.ui.fragment.image_fragment.GalleryImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryVM @Inject constructor(
    private val galleryRP: GalleryRP
    ): ViewModel() {

    // --->>> REGION: Gallery-Folder
    private var galleryFolderDataState = MutableLiveData<DataState<ArrayList<GalleryFolder>>>()
    val galleryFolderUiState: LiveData<DataState<ArrayList<GalleryFolder>>> get() = galleryFolderDataState
    private fun emitGalleryFolderUiState(isLoading: Boolean = false, data: ArrayList<GalleryFolder>? = null, error: String? = null){
        val dataState = DataState(isLoading, data, error)
        galleryFolderDataState.postValue(dataState)
    }
    private fun loadGalleryFolder() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                emitGalleryFolderUiState(isLoading = true)
                galleryRP.loadGalleryFolders()
            }.onSuccess { list ->
                if (list.isEmpty()) {
                    emitGalleryFolderUiState(error = "No Folder Found")
                } else {
                    emitGalleryFolderUiState(data = list)
                }
            }.onFailure {  error ->
                emitGalleryFolderUiState(error = error.toString())
            }
        }
    }


    private var galleryImageDataState = MutableLiveData<DataState<ArrayList<GalleryImage>>>()
    val galleryImageUiState: LiveData<DataState<ArrayList<GalleryImage>>> get() = galleryImageDataState
    private fun emitGalleryImageUiState(isLoading: Boolean = false, data: ArrayList<GalleryImage>? = null, error: String? = null) {
        val dataState = DataState(isLoading, data, error)
        galleryImageDataState.postValue(dataState)
    }
    fun loadGalleryImages(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
              emitGalleryImageUiState(isLoading = true)
              galleryRP.loadGalleryImages(path)
            }.onSuccess { list ->
                if (list.isEmpty()){
                    emitGalleryImageUiState(error = "Images Not Found")
                } else {
                    emitGalleryImageUiState(data = list)
                }
            }.onFailure {  error ->
                emitGalleryImageUiState(error = error.toString())
            }
        }
    }

    init {
        loadGalleryFolder()
    }

}