package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.ecmuser.models.healthmedia.HealthMediaRequest
import com.iprism.ecmuser.models.healthmedia.MainDataItem
import com.iprism.swen.adapters.HealthMediasAdapter
import com.iprism.swen.databinding.ActivityAddLabTestPatientBinding
import com.iprism.swen.databinding.FragmentHealthMediaBinding
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.repository.HealthMediaRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.viewmodels.HealthMediaViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class HealthMediaActivity : AppCompatActivity() {

    private lateinit var binding: FragmentHealthMediaBinding
    private lateinit var viewModel: HealthMediaViewModel
    private lateinit var mediasAdapter: HealthMediasAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var items = mutableListOf<MainDataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHealthMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpMediaItems()
        initViewModel()
        observeResponse()
        fetchMediaItems()
    }

    private fun setUpMediaItems() {
        mediasAdapter = HealthMediasAdapter(items as ArrayList<MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.healthMediaRv.apply {
            layoutManager = linearLayoutManager
            adapter = mediasAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreItems()
                        }
                    }
                }
            })
        }
    }

    private fun initViewModel() {
        val repository = HealthMediaRepository()
        val factory = ViewModelFactory { HealthMediaViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HealthMediaViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                        binding.noDataLl.visibility = View.GONE
                    }
                }

                is UiState.Success -> {
                    Log.d("result1", result.data.response.toString())
                    binding.progress.hideProgress()
                    binding.noDataLl.visibility = View.GONE
                    isLoading = false
                    mediasAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.mainData
                    if (newBookings.isNotEmpty()) {
                        items.addAll(newBookings)
                        mediasAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    mediasAdapter.removeLoadingFooter()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchMediaItems() {
        val userDetails = getUserDetails()
        val request = HealthMediaRequest(userDetails[User.ID]!!.toInt(), currentPage, userDetails[User.AUTH_TOKEN].toString(), Constants.MAIN_DATA_ID, "health_media")
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchHealthMedia(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage += 1
        mediasAdapter.showLoadingFooter()
        fetchMediaItems()
    }
}