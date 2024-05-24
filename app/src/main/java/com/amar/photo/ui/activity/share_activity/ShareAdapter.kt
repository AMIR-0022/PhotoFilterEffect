package com.amar.photo.ui.activity.share_activity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.databinding.ItemShareBinding

class ShareAdapter(var callback: (share: Share) -> Unit)
    : RecyclerView.Adapter<ShareAdapter.ViewHolder>(){

    private lateinit var context: Context
    private var shareList = ArrayList<Share>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareAdapter.ViewHolder {
        context = parent.context
        val binding = ItemShareBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShareAdapter.ViewHolder, position: Int) {
        holder.bind(shareList[position])
    }

    override fun getItemCount(): Int {
        return shareList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<Share>) {
        shareList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var binding: ItemShareBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(share: Share) {
                binding.share = share

                itemView.apply {
                    setOnClickListener {
                        callback.invoke(share)
                    }
                }
            }
        }

}