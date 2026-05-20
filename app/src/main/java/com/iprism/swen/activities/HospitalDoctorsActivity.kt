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
import com.iprism.swen.adapters.HospitalDoctorsAdapter
import com.iprism.swen.databinding.ActivityDoctorsBinding
import com.iprism.swen.interfaces.OnHospitalDoctorItemClickListener
import com.iprism.swen.models.hospitaldoctors.DoctorsItem
import com.iprism.swen.models.hospitaldoctors.HospitalDoctorsRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.viewmodels.HospitalDoctorsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class HospitalDoctorsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDoctorsBinding
    private lateinit var viewModel: HospitalDoctorsViewModel
    private var tag : String = ""
    private var specialityId : String = ""
    private var hospitalId : String = ""
    private var catId = ""
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var doctorsList = mutableListOf<DoctorsItem>()
    private lateinit var doctorsAdapter: HospitalDoctorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
        }
        if (intent.hasExtra("name")) {
            binding.categoryTxt.text = intent.getStringExtra("name")
            specialityId = intent.getStringExtra("id")!!
            hospitalId = intent.getStringExtra("hospitalId")!!
            catId = intent.getStringExtra("catId")!!
        }
        handleBack()
        initViewModel()
        observeDoctorsResponse()
        getDoctors()
        setUpDoctors()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setUpDoctors() {
        if (tag.equals("onlineDoctors", true)) {
            doctorsAdapter = HospitalDoctorsAdapter(doctorsList as ArrayList<DoctorsItem?>)
        } else if (tag.equals("hospitalDoctors", true)) {
            doctorsAdapter = HospitalDoctorsAdapter(doctorsList as ArrayList<DoctorsItem?>)
        }
        val linearLayoutManager = LinearLayoutManager(this)
        binding.doctorsRv.apply {
            layoutManager = linearLayoutManager
            adapter = doctorsAdapter
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
        doctorsAdapter.setOnDoctorItemClickListener(object : OnHospitalDoctorItemClickListener {
            override fun onItemClicked(doctor: DoctorsItem) {
                val intent = Intent(this@HospitalDoctorsActivity, HospitalDoctorDetailsActivity::class.java)
                if (tag.equals("onlineDoctors", true)) {
                    intent.putExtra("tag", "onlineDoctors")
                } else {
                    intent.putExtra("tag", "hospitalDoctors")
                }
                intent.putExtra("doctor", doctor)
                intent.putExtra("specialityId", specialityId)
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("catId", catId)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalDoctorsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalDoctorsViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeDoctorsResponse() {
        viewModel.doctors.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    doctorsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.doctors
                    if (newBookings.isNotEmpty()) {
                        doctorsList.addAll(newBookings)
                        doctorsAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    doctorsAdapter.removeLoadingFooter()
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getDoctors() {
        val userDetails = getUserDetails()
        val hospitalDoctorsRequest = HospitalDoctorsRequest(userDetails[User.ID].toString(), specialityId, userDetails[User.LANG].toString(), currentPage.toString(), userDetails[User.AUTH_TOKEN].toString(), hospitalId)
        NetworkRetryHelper.checkAndCallWithRetry(this, hospitalDoctorsRequest) { req ->
            viewModel.getDoctors(req)
        }
        Log.d("hospitalDoctorsRequest", hospitalDoctorsRequest.toString())
    }

    private fun loadMoreDoctors() {
        isLoading = true
        currentPage += 1
        doctorsAdapter.showLoadingFooter()
        getDoctors()
    }
}