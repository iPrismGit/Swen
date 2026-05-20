package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.adapters.HospitalsAdapter
import com.iprism.swen.databinding.ActivityHospitalsBinding
import com.iprism.swen.interfaces.OnHospitalItemClickListener
import com.iprism.swen.models.hospitals.MainDataItem
import com.iprism.swen.models.maindatahospitals.MainDataHospitalsRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.viewmodels.MainDataHospitalsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class HospitalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalsBinding
    private lateinit var viewModel: MainDataHospitalsViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var hospitals = mutableListOf<MainDataItem>()
    private lateinit var hospitalsAdapter: HospitalsAdapter
    var lat = ""
    var lon = ""
    var catId = ""
    var catName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("catId")) {
            catId = intent.getStringExtra("catId")!!
            catName = intent.getStringExtra("catName")!!
            lat = intent.getStringExtra("lat")!!
            lon = intent.getStringExtra("lon")!!
            binding.catNameTxt.text = catName
        }
        handleBack()
        initViewModel()
        observeResponse()
        setUpHospitals()
        getHospitals()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setUpHospitals() {
        hospitalsAdapter = HospitalsAdapter(hospitals as ArrayList<MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.hospitalsRv.apply {
            layoutManager = linearLayoutManager
            adapter = hospitalsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreDoctors()
                        }
                    }
                }
            })
        }
        hospitalsAdapter.setOnDoctorItemClickListener(object : OnHospitalItemClickListener {
            override fun onItemClicked(hospitalId: String) {
                val intent = Intent(this@HospitalsActivity, HospitalDetailsActivity::class.java)
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                startActivity(intent)
            }
        })
    }

    private fun handleFilterLL() {
        binding.filterLl.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { MainDataHospitalsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[MainDataHospitalsViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                    }
                }

                is UiState.Success -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    isLoading = false
                    hospitalsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.mainData
                    if (newBookings.isNotEmpty()) {
                        hospitals.addAll(newBookings)
                        hospitalsAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    isLoading = false
                    hospitalsAdapter.removeLoadingFooter()
                    //showToast(result.message)
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getHospitals() {
        val userDetails = getUserDetails()
        val request = MainDataHospitalsRequest("", userDetails[User.ID]!!.toInt(), catId.toInt(), lon, userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), lat)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getMainDataHospitals(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreDoctors() {
        isLoading = true
        currentPage += 1
        hospitalsAdapter.showLoadingFooter()
        getHospitals()
    }
}