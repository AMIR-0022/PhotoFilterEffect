package com.amar.photo.ui.home_fragment.category

interface CategoryRP {

    suspend fun getCategory(): ArrayList<Category>

}