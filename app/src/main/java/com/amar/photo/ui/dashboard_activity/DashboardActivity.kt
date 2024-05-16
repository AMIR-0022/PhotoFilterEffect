package com.amar.photo.ui.dashboard_activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.amar.photo.R
import com.amar.photo.databinding.ActivityDashboardBinding
import com.amar.photo.ui.home_fragment.HomeFragment
import com.amar.photo.ui.work_fragment.WorkFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private lateinit var adapter: DashboardAdapter
    private val viewModel: DashboardVM by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        init()
    }

    private fun init() {
        populateData()
        setClickListener()
    }

    private fun populateData() {
        adapter = DashboardAdapter { previousPos, selectivePos, selectiveMenu, item ->
            when (selectivePos) {
                0 -> {
                    Toast.makeText(this, selectiveMenu, Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    findNavController(R.id.nav_container_main).navigate(R.id.homeFragment)
                }
                2 -> {
                    findNavController(R.id.nav_container_main).navigate(R.id.workFragment)
                }
            }
        }

        binding.rvBottomNav.adapter = adapter
        adapter.setData(viewModel.dashboardBottomNavItems())
    }

    private fun setClickListener() {

    }

//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frame_layout, fragment)
//        fragmentTransaction.commit()
//    }

}