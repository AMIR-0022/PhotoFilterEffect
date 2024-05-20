package com.amar.photo.ui.fragment.home_fragment.category

interface CategoryRP {

    suspend fun getCategory(): ArrayList<Category>

}