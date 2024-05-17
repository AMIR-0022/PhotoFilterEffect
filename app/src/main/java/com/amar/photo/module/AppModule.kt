package com.amar.photo.module

import com.amar.photo.ui.home_fragment.category.CategoryImpRP
import com.amar.photo.ui.home_fragment.category.CategoryRP
import com.amar.photo.ui.home_fragment.thumb.ThumbImpRP
import com.amar.photo.ui.home_fragment.thumb.ThumbRP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providesCategoryRepository() : CategoryRP{
        return CategoryImpRP()
    }

    @Provides
    fun providesThumbRepository() : ThumbRP {
        return ThumbImpRP()
    }

}