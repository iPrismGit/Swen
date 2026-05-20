package com.iprism.swen.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.swen.adapters.WalletHistoryAdapter
import com.iprism.swen.databinding.ActivityWalletHistoryBinding
import com.iprism.swen.models.wallethistory.ResponseItem
import com.iprism.swen.models.wallethistory.WalletHistoryRequest
import com.iprism.swen.repository.WalletRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.viewmodels.ViewModelFactory
import com.iprism.swen.viewmodels.WalletHistoryViewModel

class WalletHistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWalletHistoryBinding
    private lateinit var viewModel: WalletHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        initViewModel()
        observeResponse()
        fetchWalletHistory()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val repository = WalletRepository()
        val factory = ViewModelFactory { WalletHistoryViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[WalletHistoryViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    setupWalletHistory(result.data.response)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupWalletHistory(items : List<ResponseItem>) {
        val addressListAdapter = WalletHistoryAdapter(items)
        binding.walletHistoryRv.layoutManager = LinearLayoutManager(this)
        binding.walletHistoryRv.adapter = addressListAdapter
    }

    private fun fetchWalletHistory() {
        val userDetails = getUserDetails()
        val request = WalletHistoryRequest(userDetails[User.ID]!!.toInt(), userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchWalletHistory(req)
        }
        Log.d("requestLoading", request.toString())
    }
}