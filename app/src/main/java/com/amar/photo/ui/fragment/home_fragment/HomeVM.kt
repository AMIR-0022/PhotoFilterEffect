package com.amar.photo.ui.fragment.home_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amar.photo.data_state.DataState
import com.amar.photo.ui.fragment.home_fragment.category.Category
import com.amar.photo.ui.fragment.home_fragment.category.CategoryRP
import com.amar.photo.ui.fragment.home_fragment.thumb.Thumb
import com.amar.photo.ui.fragment.home_fragment.thumb.ThumbRP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val thumbRP: ThumbRP,
    private val categoryRP: CategoryRP
    ) : ViewModel() {

    // --->>> REGION: Category-Data
    private var categoryDataState = MutableLiveData<DataState<ArrayList<Category>>>()
    val categoryUiState: LiveData<DataState<ArrayList<Category>>> get() = categoryDataState
    private var categoryPreviousPos = 0
    private fun emitCategoryUiState(isLoading: Boolean = false, data: ArrayList<Category>? = null, error: String? = null) {
        val dataState = DataState(isLoading, data, error)
        categoryDataState.postValue(dataState)
    }

    private fun loadCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                emitCategoryUiState(isLoading = true)
                categoryRP.getCategory()
            }.onSuccess { list ->
                if (list.isEmpty()) {
                    emitCategoryUiState(error = "No Category Found")
                } else {
                    emitCategoryUiState(data = list)
                }
            }.onFailure { error ->
                emitCategoryUiState(error = error.toString())
            }
        }
    }

    fun activateCategory(position: Int) {
        // Get the current list of categories
        val currentData = categoryDataState.value?.data
        currentData?.let { list ->

            // change the value of item check or not
            list[position].isChecked = true
            list[categoryPreviousPos].isChecked = false
            categoryPreviousPos = position

            // update the item list
            emitCategoryUiState(data = list)
        }
    }


    // --->>> REGION: Thumb-Data
    private var thumbImageDataState = MutableLiveData<DataState<ArrayList<Thumb>>>()
    val thumbImageUiState: LiveData<DataState<ArrayList<Thumb>>> get() = thumbImageDataState
    private fun emitThumbImageUiState(isLoading: Boolean = false, thumbList: ArrayList<Thumb>? = null, error: String? = null) {
        val dataState = DataState(isLoading, thumbList, error)
        thumbImageDataState.postValue(dataState)
    }

    fun loadThumbImages(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                emitThumbImageUiState(isLoading = true)
                thumbRP.getThumbEffects(position)
            }.onSuccess {
                if (it.isEmpty()) {
                    emitThumbImageUiState(error = "No Images Found")
                } else {
                    emitThumbImageUiState(thumbList = it)
                }
            }.onFailure {
                emitThumbImageUiState(error = it.toString())
            }
        }
    }


    init {
        loadCategoryList()
        loadThumbImages(0)
    }

}