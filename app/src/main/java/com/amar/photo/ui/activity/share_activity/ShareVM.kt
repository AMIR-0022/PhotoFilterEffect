package com.amar.photo.ui.activity.share_activity

import androidx.lifecycle.ViewModel
import com.amar.photo.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareVM @Inject constructor(): ViewModel() {

    fun loadSocialShare(): ArrayList<Share> {
        val list = ArrayList<Share>()
        list.add(Share("Facebook", R.drawable.img_facebook))
        list.add(Share("Twitter", R.drawable.img_twitter))
        list.add(Share("Instagram", R.drawable.img_instagram))
        list.add(Share("WhatsApp", R.drawable.img_whatsapp))
        return list
    }

}