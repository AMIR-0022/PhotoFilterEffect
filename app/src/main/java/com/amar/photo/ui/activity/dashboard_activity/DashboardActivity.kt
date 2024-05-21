package com.amar.photo.ui.activity.dashboard_activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.amar.photo.R
import com.amar.photo.constants.AppConstants
import com.amar.photo.databinding.ActivityDashboardBinding
import com.amar.photo.utils.AppUtils.navigateToFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private lateinit var adapter: DashboardAdapter
    private val viewModel: DashboardVM by viewModels()

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        init()
    }

    private fun init() {
        populateData()
        populateView()
    }

    private fun populateData() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container_main) as NavHostFragment
        navController = navHostFragment.navController
        adapter = DashboardAdapter { previousPos, selectivePos, selectiveMenu, item ->
            when (selectivePos) {
                0 -> {
                    Toast.makeText(this, selectiveMenu, Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    navigateToFragment(R.id.homeFragment, navController)
                }
                2 -> {
                    navigateToFragment(R.id.workFragment, navController)
                }
            }
        }

        binding.rvBottomNav.adapter = adapter
        adapter.setData(viewModel.dashboardBottomNavItems())
    }

    private fun populateView() {
        when(AppConstants.SELECT_DASHBOARD_NAV_ITEM) {
            1 -> {
                navigateToFragment(R.id.homeFragment, navController)
            }
            2 -> {
                navigateToFragment(R.id.workFragment, navController)
            }
        }
    }

//    private fun navigateToFragment(resId: Int) {
//        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container_main) as NavHostFragment
//        navController = navHostFragment.navController
//        navOptions = NavOptions.Builder()
//            .setPopUpTo(navController.currentDestination!!.id, true)
//            .build()
//        navController.navigate(resId, null, navOptions)
//    }

//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frame_layout, fragment)
//        fragmentTransaction.commit()
//    }

}