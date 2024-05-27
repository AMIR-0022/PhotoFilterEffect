package com.amar.photo.ui.fragment.adjust_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amar.photo.R
import com.amar.photo.databinding.FragmentAdjustmentBinding

class AdjustmentFragment : Fragment() {

    private lateinit var binding: FragmentAdjustmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_adjustment, container, false)
        binding = FragmentAdjustmentBinding.bind(myView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        populateData()
        setClickListeners()
    }

    private fun setClickListeners() {

    }

    private fun populateData() {

    }



}