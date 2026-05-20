package com.iprism.medrayder.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.adapters.DoctorsAdapter
import com.iprism.medrayder.databinding.ActivityDoctorsBinding
import com.iprism.medrayder.interfaces.OnDoctorItemClickListener
import com.iprism.medrayder.models.onlinedoctors.DoctorsItem
import com.iprism.medrayder.models.onlinedoctors.OnlineDoctorRequest
import com.iprism.medrayder.repository.OnlineDoctorRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.OnlineDoctorsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class DoctorsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorsBinding
    private lateinit var viewModel: OnlineDoctorsViewModel
    private var tag: String = ""
    private var specialityId: String = ""
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var doctorsList = mutableListOf<DoctorsItem>()
    private lateinit var doctorsAdapter: DoctorsAdapter

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
        if (tag.equals("onlineDoctors", true)) {
            doctorsAdapter = DoctorsAdapter(doctorsList as ArrayList<DoctorsItem?>, false)
        } else if (tag.equals("hospitalDoctors", true)) {
            doctorsAdapter = DoctorsAdapter(doctorsList as ArrayList<DoctorsItem?>, true)
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
        doctorsAdapter.setOnDoctorItemClickListener(object : OnDoctorItemClickListener {
            override fun onItemClicked(doctor: DoctorsItem) {
                val intent = Intent(this@DoctorsActivity, DoctorDetailsActivity::class.java)
                if (tag.equals("onlineDoctors", true)) {
                    intent.putExtra("tag", "onlineDoctors")
                } else {
                    intent.putExtra("tag", "hospitalDoctors")
                }
                intent.putExtra("doctor", doctor)
                intent.putExtra("specialityId", specialityId)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = OnlineDoctorRepository()
        val factory = ViewModelFactory { OnlineDoctorsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[OnlineDoctorsViewModel::class.java]
    }

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
        val request = OnlineDoctorRequest(
            userDetails[User.ID].toString(),
            specialityId,
            userDetails[User.LANG].toString(),
            currentPage.toString(),
            userDetails[User.AUTH_TOKEN].toString()
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getOnlineDoctors(req)
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