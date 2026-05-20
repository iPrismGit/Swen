package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.activities.LabDetailsActivity
import com.iprism.medrayder.adapters.LabTestCentersAdapter
import com.iprism.medrayder.adapters.PharmaciesAdapter
import com.iprism.medrayder.databinding.ActivityLabTestsSearchBinding
import com.iprism.medrayder.databinding.ActivityPharmacySearchBinding
import com.iprism.medrayder.databinding.FragmentLabTestsBinding
import com.iprism.medrayder.interfaces.OnDiagnosticItemClickListener
import com.iprism.medrayder.interfaces.OnPharmacyItemClickListener
import com.iprism.medrayder.models.labcenters.LabCentersRequest
import com.iprism.medrayder.models.labcenters.MainDataItem
import com.iprism.medrayder.models.pharmacies.PharmaciesRequest
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.repository.PharmaciesRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.LabsViewModel
import com.iprism.medrayder.viewmodels.LocationViewModel
import com.iprism.medrayder.viewmodels.PharmaciesViewModel
import com.iprism.medrayder.viewmodels.SearchTextViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class PharmacySearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPharmacySearchBinding
    private lateinit var viewModel: PharmaciesViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var pharmacies = mutableListOf<com.iprism.medrayder.models.pharmacies.MainDataItem>()
    private lateinit var pharmaciesAdapter: PharmaciesAdapter
    var lat = ""
    var lon = ""
    var search = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmacySearchBinding.inflate(layoutInflater)
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
            getPharmacies(binding.searchEt.text.toString())
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
                pharmacies.clear()
                getPharmacies(binding.searchEt.text.toString().trim())
            }
            true
        }
    }

    private fun setUpHospitals() {
        pharmaciesAdapter = PharmaciesAdapter(pharmacies as ArrayList<com.iprism.medrayder.models.pharmacies.MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.pharmaciesRv.apply {
            layoutManager = linearLayoutManager
            adapter = pharmaciesAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMorePharmacies()
                        }
                    }
                }
            })
        }
        pharmaciesAdapter.setOnDoctorItemClickListener(object : OnPharmacyItemClickListener {
            override fun onItemClicked(pharmacyId: String, orderType : String) {
                val intent = Intent(this@PharmacySearchActivity, PharmacyDetailsActivity::class.java)
                intent.putExtra("pharmacyId", pharmacyId)
                intent.putExtra("orderType", orderType)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = PharmaciesRepository()
        val factory = ViewModelFactory { PharmaciesViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[PharmaciesViewModel::class.java]
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
                    pharmaciesAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.mainData
                    if (newBookings.isNotEmpty()) {
                        pharmacies.addAll(newBookings)
                        pharmaciesAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    isLoading = false
                    pharmaciesAdapter.removeLoadingFooter()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getPharmacies(search : String) {
        val userDetails = getUserDetails()
        val request = PharmaciesRequest(search, userDetails[User.ID]!!.toInt(), lon, userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), lat)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchPharmacies(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMorePharmacies() {
        isLoading = true
        currentPage += 1
        pharmaciesAdapter.showLoadingFooter()
        getPharmacies(binding.searchEt.text.toString().trim())
    }
}