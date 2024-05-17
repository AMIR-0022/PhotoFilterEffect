package com.amar.photo.ui.home_fragment.thumb

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.databinding.ItemThumbBinding

class ThumbAdapter(private var callback: (position: Int, thumb: Thumb) -> Unit)
    : RecyclerView.Adapter<ThumbAdapter.ViewHolder>(){

    private lateinit var context: Context
    private var thumbList: ArrayList<Thumb> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ItemThumbBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(thumbList[position], position)
    }

    override fun getItemCount(): Int {
        return thumbList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<Thumb>){
        thumbList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemThumbBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(thumb: Thumb, position: Int) {
                binding.thumb = thumb
                itemView.apply {
                    setOnClickListener {
                        callback.invoke(position, thumb)
                    }
                }
            }
        }

}