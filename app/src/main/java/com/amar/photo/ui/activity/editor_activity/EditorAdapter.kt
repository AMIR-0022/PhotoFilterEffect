package com.amar.photo.ui.activity.editor_activity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.databinding.ItemNavigationBinding
import com.amar.photo.utils.SELECTIVE_EDITOR_NAV_ITEM

class EditorAdapter(var callback: (previousPos: Int, selectivePos: Int, selectiveMenu: String, item: Editor) -> Unit):
    RecyclerView.Adapter<EditorAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var itemList: ArrayList<Editor>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ItemNavigationBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<Editor>) {
        itemList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemNavigationBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(editor: Editor, position: Int) {

                if (SELECTIVE_EDITOR_NAV_ITEM == position) {
                    binding.itemImage.setImageResource(editor.activeIcon)
                } else {
                    binding.itemImage.setImageResource(editor.inactiveIcon)
                }

                itemView.apply {
                    setOnClickListener {
                        val prePos = SELECTIVE_EDITOR_NAV_ITEM
                        SELECTIVE_EDITOR_NAV_ITEM = position

                        callback.invoke(prePos, SELECTIVE_EDITOR_NAV_ITEM, editor.title, editor)

                        notifyItemChanged(position)
                        notifyItemChanged(prePos)
                        notifyDataSetChanged()
                    }
                }


            }
    }

}