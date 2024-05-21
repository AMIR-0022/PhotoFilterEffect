package com.amar.photo.ui.fragment.work_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.databinding.ItemImageBinding
import com.amar.photo.ui.fragment.image_fragment.GalleryImage

class WorkAdapter(private var callback: (savedWork: GalleryImage) -> Unit)
    : RecyclerView.Adapter<WorkAdapter.ViewHolder>(){

    private lateinit var context: Context
    private var workImageList = ArrayList<GalleryImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkAdapter.ViewHolder {
        context = parent.context
        val binding = ItemImageBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkAdapter.ViewHolder, position: Int) {
        holder.bind(workImageList[position])
    }

    override fun getItemCount(): Int {
        return workImageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<GalleryImage>){
        workImageList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var binding: ItemImageBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(savedWork: GalleryImage){
                binding.model = savedWork

                itemView.apply {
                    setOnClickListener {
                        callback(savedWork)
                    }
                }
            }
        }
}