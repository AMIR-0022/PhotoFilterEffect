package com.amar.photo.ui.fragment.adjust_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amar.photo.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdjustVM @Inject constructor(): ViewModel() {

    private var adjustList = MutableLiveData<ArrayList<Adjust>>()
    val adjustData: LiveData<ArrayList<Adjust>> get() = adjustList

    init {
        adjustList.postValue(getList())
    }

    private fun getList(): ArrayList<Adjust> {
        val list = ArrayList<Adjust>()
        list.add(Adjust("Brightness", -50, 50, 0, FilterType.BRIGHTNESS))
        list.add(Adjust("Contrast", 0, 200, 80, FilterType.CONTRAST))
        list.add(Adjust("Saturation",  0, 200, 100, FilterType.SATURATION))
        list.add(Adjust("Vignette", 0, 100, 50, FilterType.VIGNETTE))
        list.add(Adjust("Sharpen", 0, 200, 0, FilterType.SHARPNESS))
        list.add(Adjust("Hue", -50, 50, 0, FilterType.HUE))
        list.add(Adjust("Blur", 0, 200, 0, FilterType.BLUR))
        return list
    }

    fun updateFilterProgress(filterType: FilterType, progress: Int) {
        val updatedFilters = adjustList.value?.map {
            if (it.type == filterType) it.copy(progress = progress) else it
        }
        adjustList.value = (updatedFilters as ArrayList<Adjust>?)
    }

    fun updateAdjustAtIndex(index: Int, progress: Int) {
        val currentList: ArrayList<Adjust> = adjustList.value!!
        try {
            currentList[index].progress = progress
            adjustList.postValue(currentList)
        } catch (ex: Exception) {
            Log.d(TAG, "updateAdjustAtIndex: ${ex.message}")
        }
    }

}