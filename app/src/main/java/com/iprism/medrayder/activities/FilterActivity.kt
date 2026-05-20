package com.iprism.medrayder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.medrayder.adapters.FilterCatsAdapter
import com.iprism.medrayder.adapters.FilterSubCatsAdapter
import com.iprism.medrayder.databinding.ActivityFilterBinding

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