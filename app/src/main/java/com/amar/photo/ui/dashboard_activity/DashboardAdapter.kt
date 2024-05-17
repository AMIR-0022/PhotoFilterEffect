package com.amar.photo.ui.dashboard_activity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.constants.Constants
import com.amar.photo.databinding.ItemBottomNavBinding

class DashboardAdapter(var callback: (previousPos: Int, selectivePos: Int, selectiveMenu: String, item: Dashboard) -> Unit):
    RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var itemList: ArrayList<Dashboard>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.ViewHolder {
        context = parent.context
        val binding = ItemBottomNavBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<Dashboard>) {
        itemList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemBottomNavBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(dashboard: Dashboard, position: Int) {

                if (Constants.selectedNavItem == position) {
                    binding.itemImage.setImageResource(dashboard.activeIcon)
                } else {
                    binding.itemImage.setImageResource(dashboard.inactiveIcon)
                }

                itemView.apply {
                    setOnClickListener{
                        val prePos = Constants.selectedNavItem
                        Constants.selectedNavItem = position

                        callback.invoke(prePos, Constants.selectedNavItem, dashboard.title, dashboard)

                        notifyItemChanged(position)
                        notifyItemChanged(prePos)
                        notifyDataSetChanged()
                    }
                }


            }
    }

}