package com.amar.photo.ui.fragment.filter_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.databinding.ItemEffectBinding

class FilterAdapter(private var callback: (filter: Filter) -> Unit)
    : RecyclerView.Adapter<FilterAdapter.ViewHolder>(){

    private lateinit var context: Context
    private var filterList: ArrayList<Filter> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ItemEffectBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filterList[position])
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<Filter>){
        filterList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemEffectBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(filter: Filter) {
                binding.itemSavedImage.setImageBitmap(filter.filteredImage)
                itemView.apply {
                    setOnClickListener {
                        callback.invoke(filter)
                    }
                }
            }
        }

}