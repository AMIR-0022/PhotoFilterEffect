package com.amar.photo.state

data class DataState <T> (
    val isLoading: Boolean,
    val data: T?,
    val error: String?
)