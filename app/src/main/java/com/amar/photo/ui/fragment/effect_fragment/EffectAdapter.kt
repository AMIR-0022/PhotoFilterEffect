package com.amar.photo.ui.fragment.effect_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.databinding.ItemEffectBinding
import com.amar.photo.ui.fragment.home_fragment.thumb.Thumb
import com.amar.photo.utils.imageFromUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class EffectAdapter(private var callback: (thumb: Thumb) -> Unit)
    : RecyclerView.Adapter<EffectAdapter.ViewHolder>(){

    private lateinit var context: Context
    private var thumbList: ArrayList<Thumb> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ItemEffectBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(thumbList[position])
    }

    override fun getItemCount(): Int {
        return thumbList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<Thumb>){
        thumbList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemEffectBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(thumb: Thumb) {

                Glide.with(context)
                    .load(thumb.cover)
                    .apply(RequestOptions()
                    .fitCenter())
                    .centerCrop()
                    .into(binding.itemSavedImage)

                itemView.apply {
                    setOnClickListener {
                        callback.invoke(thumb)
                    }
                }
            }
        }

}