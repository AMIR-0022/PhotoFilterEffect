package com.amar.photo.ui.activity.editor_activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.amar.photo.R
import com.amar.photo.databinding.ActivityEditorBinding
import com.amar.photo.utils.AppUtils.navigateToFragment
import com.amar.photo.utils.SELECTIVE_EDITOR_NAV_ITEM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding

    private lateinit var adapter: EditorAdapter
    private val viewModel: EditorVM by viewModels()

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor)

        init()
    }

    private fun init(){
        populateData()
        setClickListeners()
    }

    private fun setClickListeners(){
        binding.customAppBar.setOnBackIconClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun populateData(){
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container_editor) as NavHostFragment
        navController = navHostFragment.navController
        navigateToFragment(R.id.effectFragment, navController)
        adapter = EditorAdapter() {previousPos, selectivePos, selectiveMenu, item ->
            when (selectivePos) {
                0 -> {
                    navigateToFragment(R.id.effectFragment, navController)
                }
                1 -> {
                    navigateToFragment(R.id.filterFragment, navController)
                }
                2 -> {
                    navigateToFragment(R.id.adjustmentFragment, navController)
                }
            }
        }
        binding.rvBottomNav.adapter = adapter
        adapter.setData(viewModel.editorBottomNavItems())
    }

    override fun onDestroy() {
        super.onDestroy()
        SELECTIVE_EDITOR_NAV_ITEM = 0
    }

}