package com.iprism.swen.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iprism.swen.databinding.ActivityFilterBinding

class FilterActivity : Fragment() {

    private lateinit var binding : ActivityFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityFilterBinding.inflate(layoutInflater)
        handleApplyFilterBtn()
        handleBack()
        return binding.root
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            parentFragmentManager.popBackStack()
        })
    }

    private fun handleApplyFilterBtn() {
        binding.applyFilterBtn.setOnClickListener(View.OnClickListener {
            parentFragmentManager.popBackStack()
        })
    }
}