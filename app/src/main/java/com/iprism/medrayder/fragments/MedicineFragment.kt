package com.iprism.medrayder.fragments

import MedicinePagerAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.medrayder.R
import com.iprism.medrayder.activities.HospitalsSearchActivity
import com.iprism.medrayder.activities.LabTestsSearchActivity
import com.iprism.medrayder.activities.PharmacySearchActivity
import com.iprism.medrayder.databinding.FragmentMedicineBinding
import com.iprism.medrayder.viewmodels.SearchTextViewModel

class MedicineFragment : Fragment() {

    private lateinit var binding: FragmentMedicineBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMedicineBinding.inflate(layoutInflater)
        handlePharmaciesLL()
        handleWellnessProductsLL()
        val adapter = MedicinePagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(0, false)
        val viewModel1 = ViewModelProvider(requireActivity())[SearchTextViewModel::class.java]
        viewModel1.searchData.observe(viewLifecycleOwner) { result ->
            val currentItem = binding.viewPager.currentItem
            if (currentItem == 0) {
            } else if (currentItem == 1) {
                val intent = Intent(requireContext(), PharmacySearchActivity::class.java)
                intent.putExtra("lat", result.lat)
                intent.putExtra("lon", result.lon)
                intent.putExtra("search", result.search)
                startActivity(intent)
            }
        }
        return binding.root
    }

    private fun handleWellnessProductsLL() {
        binding.wellnessProductsLl.setOnClickListener(View.OnClickListener {
            binding.viewPager.setCurrentItem(0, false)
            binding.wellnessProductsTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            binding.pharmaciesTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.wellnessProductsDivider.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
            binding.pharmaciesDivider.setBackgroundColor(Color.parseColor("#A8A8A8"))
            binding.wellnessProductsDivider.visibility = View.VISIBLE
            binding.pharmaciesDivider.visibility = View.GONE
        })
    }

    private fun handlePharmaciesLL() {
        binding.pharmaciesLl.setOnClickListener(View.OnClickListener {
            binding.viewPager.setCurrentItem(1, false)
            binding.pharmaciesTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            binding.wellnessProductsTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.pharmaciesDivider.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
            binding.wellnessProductsDivider.setBackgroundColor(Color.parseColor("#A8A8A8"))
            binding.pharmaciesDivider.visibility = View.VISIBLE
            binding.wellnessProductsDivider.visibility = View.GONE
        })
    }
}