package com.iprism.medrayder.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iprism.medrayder.R
import com.iprism.medrayder.activities.AddressActivity
import com.iprism.medrayder.activities.HomeActivity
import com.iprism.medrayder.adapters.AddressListAdapter
import com.iprism.medrayder.databinding.FragmentLocationBottomSheetBinding
import com.iprism.medrayder.interfaces.OnAddressItemClickListener
import com.iprism.medrayder.models.addresslist.AddressItem
import com.iprism.medrayder.models.addresslist.AddressListRequest
import com.iprism.medrayder.repository.AddressRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.AddressListViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory


class LocationBottomSheet(var type: String) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentLocationBottomSheetBinding
    private lateinit var viewModel: AddressListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBottomSheetBinding.inflate(layoutInflater)
        initViewModel()
        observeResponse()
        fetchAddressList()
        handleContinueBtn()
        handleAddNewAddressBtn()
        if (type != "home") {
            isCancelable = false
        } else {
            binding.locationOffLl.visibility = View.GONE
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            if (type.equals("locationMain", true)) {
                openAppSettings()
            } else if (type.equals("location", true)) {
                (activity as? HomeActivity)?.askPermission()
                dialog!!.dismiss()
            }
        })
    }

    private fun handleAddNewAddressBtn() {
        binding.addNewAddressBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireContext(), AddressActivity::class.java)
            intent.putExtra("tag", "home")
            startActivity(intent)
        })
    }

    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun initViewModel() {
        val repository = AddressRepository()
        val factory = ViewModelFactory { AddressListViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[AddressListViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    //binding.progress.showProgress()
                }

                is UiState.Success -> {
                    //binding.progress.hideProgress()
                    setupAddressList(result.data.response.address)
                }

                is UiState.Error -> {
                    requireContext().showToast(result.message)
                    //binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupAddressList(coupons: List<AddressItem>) {
        val addressListAdapter = AddressListAdapter(coupons, "home")
        binding.addressListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.addressListRv.adapter = addressListAdapter
        addressListAdapter.setOnAddressItemClickListener(object : OnAddressItemClickListener {
            override fun onItemClicked(address: AddressItem) {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                intent.putExtra("address", address)
                startActivity(intent)
                activity?.finish()
            }
        })
    }

    private fun fetchAddressList() {
        val userDetails = requireContext().getUserDetails()
        val request = AddressListRequest(
            userDetails[User.ID]!!.toInt(),
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.LANG].toString()
        )
        viewModel.fetchAddressList(request)
        Log.d("requestLoading", request.toString())
    }
}