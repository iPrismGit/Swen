package com.iprism.medrayder.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.medrayder.activities.HospitalDetailsActivity
import com.iprism.medrayder.activities.HospitalMedicineProductsActivity
import com.iprism.medrayder.activities.PrescriptionActivity
import com.iprism.medrayder.adapters.PharmacyCategoriesAdapter
import com.iprism.medrayder.databinding.FragmentHospitalMedicineBinding
import com.iprism.medrayder.interfaces.OnPharmacyCatItemClickListener
import com.iprism.medrayder.models.hospitalmedicines.HospitalMedicinesRequest
import com.iprism.medrayder.models.pharmacyDetails.PharmacyCategory
import com.iprism.medrayder.repository.HospitalsMedicineRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.viewmodels.HospitalMedicineViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class HospitalMedicineFragment(var hospitalId : String, var orderType : String) : Fragment() {

    private lateinit var binding : FragmentHospitalMedicineBinding
    private lateinit var viewModel: HospitalMedicineViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHospitalMedicineBinding.inflate(layoutInflater)
        handleMedicine()
        handlePrescriptionLL()
        initViewModel()
        observeResponse()
        val userDetails = requireContext().getUserDetails()
        val request = HospitalMedicinesRequest(userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchHospitalMedCategories(req)
        }
        Log.d("requestLoading", request.toString())
        return binding.root
    }

    private fun handlePrescriptionLL() {
        binding.prescriptionLl.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireContext(), PrescriptionActivity::class.java)
            intent.putExtra("tag", "hospitalPharmacy")
            intent.putExtra("hospitalId", hospitalId)
            intent.putExtra("orderType", orderType)
            startActivity(intent)
        })
    }

    private fun setupPharmacyCategoriesAdapter(pharmacyCategories : List<PharmacyCategory>) {
        val pharmacyCategoriesAdapter = PharmacyCategoriesAdapter(pharmacyCategories)
        binding.categoriesRv.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.categoriesRv.adapter = pharmacyCategoriesAdapter
        pharmacyCategoriesAdapter.setOnDoctorItemClickListener(object :
            OnPharmacyCatItemClickListener {
            override fun onItemClicked(catId: String, catName : String) {
                val intent = Intent(requireContext(), HospitalMedicineProductsActivity::class.java)
                intent.putExtra("catId", catId)
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("catName", catName)
                intent.putExtra("orderType", orderType)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalsMedicineRepository()
        val factory = ViewModelFactory { HospitalMedicineViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalMedicineViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    setupPharmacyCategoriesAdapter(result.data.response.pharmacyCategories)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun handleMedicine() {
        (activity as? HospitalDetailsActivity)?.setView("medicine")
    }
}