package com.amar.photo.ui.activity.dashboard_activity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photo.databinding.ItemNavigationBinding
import com.amar.photo.utils.SELECT_DASHBOARD_NAV_ITEM

class DashboardAdapter(var callback: (previousPos: Int, selectivePos: Int, selectiveMenu: String, item: Dashboard) -> Unit):
    RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var itemList: ArrayList<Dashboard>

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
    fun setData(list: ArrayList<Dashboard>) {
        itemList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemNavigationBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(dashboard: Dashboard, position: Int) {

                if (SELECT_DASHBOARD_NAV_ITEM == position) {
                    binding.itemImage.setImageResource(dashboard.activeIcon)
                } else {
                    binding.itemImage.setImageResource(dashboard.inactiveIcon)
                }

                itemView.apply {
                    setOnClickListener{
                        val prePos = SELECT_DASHBOARD_NAV_ITEM
                        SELECT_DASHBOARD_NAV_ITEM = position

                        callback.invoke(prePos, SELECT_DASHBOARD_NAV_ITEM, dashboard.title, dashboard)

                        notifyItemChanged(position)
                        notifyItemChanged(prePos)
                        notifyDataSetChanged()
                    }
                }


            }
    }

}