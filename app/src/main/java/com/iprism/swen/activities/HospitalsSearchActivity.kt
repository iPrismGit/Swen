package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.adapters.HospitalsAdapter
import com.iprism.swen.databinding.ActivityHospitalsSearchBinding
import com.iprism.swen.interfaces.OnHospitalItemClickListener
import com.iprism.swen.models.hospitals.HospitalsRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.viewmodels.HospitalsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class HospitalsSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalsSearchBinding
    private lateinit var viewModel: HospitalsViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var hospitals = mutableListOf<com.iprism.swen.models.hospitals.MainDataItem>()
    private lateinit var hospitalsAdapter: HospitalsAdapter
    var lat = ""
    var lon = ""
    var search = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalsSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("lat")) {
            lat = intent.getStringExtra("lat")!!
            lon = intent.getStringExtra("lon")!!
            search = intent.getStringExtra("search")!!
            binding.searchEt.setText(search)
        }
        setUpHospitals()
        initViewModel()
        observeResponse()
        if (search.isNotEmpty()) {
            getHospitals(binding.searchEt.text.toString().trim())
        } else {
            showKeyboard()
        }
        handleSearchEt()
        handleBackImg()
    }

    private fun handleBackImg() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showKeyboard() {
        binding.searchEt.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun handleSearchEt() {
        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.hideSoftInputFromWindow(binding.mainCl.windowToken, 0)
                currentPage = 1
                isLastPage = false
                hospitals.clear()
                getHospitals(binding.searchEt.text.toString().trim())
            }
            true
        }
    }

    private fun setUpHospitals() {
        hospitalsAdapter = HospitalsAdapter(hospitals as ArrayList<com.iprism.swen.models.hospitals.MainDataItem?>)
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
                val intent = Intent(this@HospitalsSearchActivity, HospitalDetailsActivity::class.java)
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalsViewModel::class.java]
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
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getHospitals(search : String) {
        val userDetails = getUserDetails()
        val request = HospitalsRequest(search, userDetails[User.ID]!!.toInt(), lon, userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), lat, "view", emptyList())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getHospitals(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreDoctors() {
        isLoading = true
        currentPage += 1
        hospitalsAdapter.showLoadingFooter()
        getHospitals(binding.searchEt.text.toString().trim())
    }
}