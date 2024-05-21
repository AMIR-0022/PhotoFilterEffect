package com.amar.photo.data_state

data class DataState <T> (
    val isLoading: Boolean,
    val data: T?,
    val error: String?
)