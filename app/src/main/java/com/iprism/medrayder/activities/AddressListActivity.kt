package com.iprism.medrayder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.medrayder.adapters.AddressListAdapter
import com.iprism.medrayder.databinding.ActivityAddressListBinding
import com.iprism.medrayder.interfaces.OnAddressItemClickListener
import com.iprism.medrayder.models.addresslist.AddressItem
import com.iprism.medrayder.models.addresslist.AddressListRequest
import com.iprism.medrayder.repository.AddressRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.AddressListViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class AddressListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddressListBinding
    private lateinit var viewModel: AddressListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleAddNewAddrresBtn()
        initViewModel()
        observeResponse()
    }

    override fun onResume() {
        super.onResume()
        fetchAddressList()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleAddNewAddrresBtn() {
        binding.addNewAddressBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, AddressActivity::class.java))
        })
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
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.addNewAddressBtn.visibility = View.VISIBLE
                    setupAddressList(result.data.response.address)
                }

                is UiState.Error -> {
                    binding.addNewAddressBtn.visibility = View.VISIBLE
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupAddressList(coupons : List<AddressItem>) {
        val addressListAdapter = AddressListAdapter(coupons, "address")
        binding.addressListRv.layoutManager = LinearLayoutManager(this)
        binding.addressListRv.adapter = addressListAdapter
        addressListAdapter.setOnAddressItemClickListener(object : OnAddressItemClickListener {
            override fun onItemClicked(address: AddressItem) {
            }
        })
    }

    private fun fetchAddressList() {
        val userDetails = getUserDetails()
        val addressListRequest = AddressListRequest(userDetails[User.ID]!!.toInt(), userDetails[User.AUTH_TOKEN].toString(), userDetails[User.LANG].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, addressListRequest) { req ->
            viewModel.fetchAddressList(req)
        }
        Log.d("requestLoading", addressListRequest.toString())
    }
}