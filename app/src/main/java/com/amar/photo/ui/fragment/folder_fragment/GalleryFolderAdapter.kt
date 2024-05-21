package com.amar.photo.ui.fragment.folder_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.databinding.ItemFolderBinding

class GalleryFolderAdapter(private var callback: (folder: GalleryFolder) -> Unit):
    RecyclerView.Adapter<GalleryFolderAdapter.ViewHolder>() {

    private var folderList: List<GalleryFolder> = arrayListOf()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryFolderAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ItemFolderBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryFolderAdapter.ViewHolder, position: Int) {
        holder.bind(folderList[position])
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<GalleryFolder>) {
        folderList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemFolderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(folder: GalleryFolder) {
            binding.folder = folder

            itemView.apply {
                setOnClickListener {
                    callback.invoke(folder)
                }
            }
        }
    }

}