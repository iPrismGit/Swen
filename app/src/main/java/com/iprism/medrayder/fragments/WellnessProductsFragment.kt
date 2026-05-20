package com.iprism.medrayder.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.medrayder.adapters.PharmacyCategoriesAdapterDummy
import com.iprism.medrayder.databinding.FragmentWellnessProductsBinding

class WellnessProductsFragment : Fragment() {

    private lateinit var binding : FragmentWellnessProductsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWellnessProductsBinding.inflate(layoutInflater)
        setupWellnessProducts()
        return binding.root
    }

    private fun setupWellnessProducts() {
        val pharmacyCategoriesAdapterDummy = PharmacyCategoriesAdapterDummy(8)
        binding.categoriesRv.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.categoriesRv.adapter = pharmacyCategoriesAdapterDummy
    }
}