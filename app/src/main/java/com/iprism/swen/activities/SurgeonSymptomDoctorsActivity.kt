package com.iprism.swen.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.models.surgeonsymptomdoctors.DoctorsItem
import com.iprism.swen.models.surgeonsymptomdoctors.SurgeonSymptomDoctorsRequest
import com.iprism.swen.viewmodels.SurgeonSymptomDoctorsViewModel
import com.iprism.swen.adapters.SurgeonSymptomDoctorsAdapter
import com.iprism.swen.databinding.ActivitySymptomsDoctorsBinding
import com.iprism.swen.interfaces.OnSurgeonSymptomDoctorItemClickListener
import com.iprism.swen.repository.SurgeonSymptomsDoctorRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.viewmodels.ViewModelFactory
import kotlin.jvm.java

class SurgeonSymptomDoctorsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySymptomsDoctorsBinding
    private lateinit var viewModel: SurgeonSymptomDoctorsViewModel
    private var symptomId = ""
    private var hospitalId = ""
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var doctorsList = mutableListOf<DoctorsItem>()
    private lateinit var doctorsAdapter: SurgeonSymptomDoctorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySymptomsDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra("symptomId")) {
            binding.categoryTxt.text = "Doctors"
            symptomId = intent.getStringExtra("symptomId")!!
            hospitalId = intent.getStringExtra("hospitalId")!!
        }
        handleBack()
        setUpDoctors()
        initViewModel()
        observeDoctorsResponse()
        getDoctors()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setUpDoctors() {
        doctorsAdapter = SurgeonSymptomDoctorsAdapter(doctorsList as ArrayList<DoctorsItem?>)
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
        doctorsAdapter.setOnDoctorItemClickListener(object :
            OnSurgeonSymptomDoctorItemClickListener {
            override fun onItemClicked(doctor: DoctorsItem) {
                val intent = Intent(this@SurgeonSymptomDoctorsActivity,
                    SurgeonSymptomDoctorProfileActivity::class.java)
                intent.putExtra("doctor", doctor)
                intent.putExtra("symptomId", symptomId)
                intent.putExtra("hospitalId", hospitalId)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = SurgeonSymptomsDoctorRepository()
        val factory = ViewModelFactory { SurgeonSymptomDoctorsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[SurgeonSymptomDoctorsViewModel::class.java]
    }

    private fun observeDoctorsResponse() {
        viewModel.response.observe(this) { result ->
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
        val request = SurgeonSymptomDoctorsRequest(
            symptomId.toInt(),
            userDetails[User.ID]!!.toInt(),
            currentPage,
            userDetails[User.AUTH_TOKEN].toString(),
            hospitalId.toInt()
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchSurgeonSymptomDoctors(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreDoctors() {
        isLoading = true
        currentPage += 1
        doctorsAdapter.showLoadingFooter()
        getDoctors()
    }
}

