package com.amar.photo.module

import android.content.Context
import com.amar.photo.ui.activity.gallery_activity.GalleryImpRP
import com.amar.photo.ui.activity.gallery_activity.GalleryRP
import com.amar.photo.ui.fragment.home_fragment.category.CategoryImpRP
import com.amar.photo.ui.fragment.home_fragment.category.CategoryRP
import com.amar.photo.ui.fragment.home_fragment.thumb.ThumbImpRP
import com.amar.photo.ui.fragment.home_fragment.thumb.ThumbRP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providesCategoryRepository() : CategoryRP {
        return CategoryImpRP()
    }

    @Provides
    fun providesThumbRepository() : ThumbRP {
        return ThumbImpRP()
    }

    @Provides
    fun providesGalleryRepository(@ApplicationContext context: Context) : GalleryRP {
        return GalleryImpRP(context)
    }

}