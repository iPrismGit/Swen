package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.adapters.HospitalsAdapter
import com.iprism.swen.databinding.FragmentHospitalBinding
import com.iprism.swen.interfaces.OnHospitalItemClickListener
import com.iprism.swen.models.hospitals.MainDataItem
import com.iprism.swen.models.surgerysymptoms.SurgerySymptomsDoctorRequest
import com.iprism.swen.repository.SurgerySymptomsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.viewmodels.SurgerySymptomHospitalsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class SurgerySymptomHospitalsActivity : AppCompatActivity() {

    private lateinit var binding : FragmentHospitalBinding
    private lateinit var viewModel: SurgerySymptomHospitalsViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var hospitals = mutableListOf<MainDataItem>()
    private lateinit var hospitalsAdapter: HospitalsAdapter
    var lat = ""
    var lon = ""
    private var symptomId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHospitalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("symptomId")) {
            symptomId = intent.getStringExtra("symptomId")!!
            lat = intent.getStringExtra("lat")!!
            lon = intent.getStringExtra("lon")!!
        }
        handleBackImg()
        initViewModel()
        observeResponse()
        getHospitals()
        setUpHospitals()
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
        hospitalsAdapter.setOnDoctorItemClickListener(object : OnHospitalItemClickListener{
            override fun onItemClicked(hospitalId: String) {
                val intent = Intent(this@SurgerySymptomHospitalsActivity, HospitalDetailsActivity::class.java)
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                startActivity(intent)
            }
        })
    }

    private fun handleBackImg() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            binding.filterLlView.visibility = View.GONE
            binding.mainCl.visibility = View.VISIBLE
        })
    }

    private fun initViewModel() {
        val repository = SurgerySymptomsRepository()
        val factory = ViewModelFactory { SurgerySymptomHospitalsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[SurgerySymptomHospitalsViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                        binding.noDataLl.visibility = View.GONE
                    }
                }

                is UiState.Success -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.noDataLl.visibility = View.GONE
                    isLoading = false
                    hospitalsAdapter.removeLoadingFooter()
                    val newBookings = result.data.hospitalResponse.mainData
                    if (newBookings.isNotEmpty()) {
                        hospitals.addAll(newBookings)
                        hospitalsAdapter.notifyDataSetChanged()
                        if (result.data.hospitalResponse.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    isLoading = false
                    hospitalsAdapter.removeLoadingFooter()
                    //requireContext().showToast(result.message)
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getHospitals() {
        val userDetails = getUserDetails()
        val request = SurgerySymptomsDoctorRequest(symptomId, "", userDetails[User.ID]!!.toInt(), lon, currentPage, userDetails[User.AUTH_TOKEN].toString(), lat)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getHospitals(req)
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