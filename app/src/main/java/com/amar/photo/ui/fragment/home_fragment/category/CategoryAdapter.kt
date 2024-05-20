package com.amar.photo.ui.fragment.home_fragment.category

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.databinding.ItemCategoryBinding

class CategoryAdapter(var callback: (item: Category) -> Unit)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){

    private lateinit var context: Context
    private var categoryList: ArrayList<Category> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position], position)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<Category>) {
        categoryList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category, position: Int) {
            binding.category = category
            itemView.apply {
                setOnClickListener {
                    callback.invoke(category)
                }
            }
        }

    }


}