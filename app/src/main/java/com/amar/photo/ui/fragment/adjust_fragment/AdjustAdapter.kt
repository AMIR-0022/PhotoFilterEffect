package com.amar.photo.ui.fragment.adjust_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.R
import com.amar.photo.databinding.ItemAdjustmentBinding
import com.amar.photo.utils.SELECTIVE_ADJUSTMENT_ITEM

class AdjustAdapter(private var callback: (adjust: Adjust) -> Unit) :
    RecyclerView.Adapter<AdjustAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var adjustList = ArrayList<Adjust>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdjustAdapter.ViewHolder {
        context = parent.context
        val binding = ItemAdjustmentBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdjustAdapter.ViewHolder, position: Int) {
        holder.bind(adjustList[position], position)
    }

    override fun getItemCount(): Int {
        return adjustList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<Adjust>) {
        adjustList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var binding: ItemAdjustmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(adjust: Adjust, position: Int) {

            if (SELECTIVE_ADJUSTMENT_ITEM == position) {
                binding.itemTitle.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_secondary
                    )
                )
            } else {
                binding.itemTitle.setTextColor(ContextCompat.getColor(context, R.color.white))

            }

            binding.itemTitle.text = adjust.title
            itemView.apply {
                setOnClickListener {
                    val prePos = SELECTIVE_ADJUSTMENT_ITEM
                    SELECTIVE_ADJUSTMENT_ITEM = position

                    callback.invoke(adjust)


                    notifyItemChanged(position)
                    notifyItemChanged(prePos)
                    notifyDataSetChanged()
                }
            }
        }
    }

}