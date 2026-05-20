package com.iprism.swen.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.activities.PharmacyDetailsActivity
import com.iprism.swen.adapters.PharmaciesAdapter
import com.iprism.swen.databinding.FragmentPharmaciesBinding
import com.iprism.swen.interfaces.OnPharmacyItemClickListener
import com.iprism.swen.models.pharmacies.MainDataItem
import com.iprism.swen.models.pharmacies.PharmaciesRequest
import com.iprism.swen.repository.PharmaciesRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.viewmodels.LocationViewModel
import com.iprism.swen.viewmodels.PharmaciesViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class PharmaciesFragment : Fragment() {

    private lateinit var binding: FragmentPharmaciesBinding
    private lateinit var viewModel: PharmaciesViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var pharmacies = mutableListOf<MainDataItem>()
    private lateinit var pharmaciesAdapter: PharmaciesAdapter
    var lat = ""
    var lon = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPharmaciesBinding.inflate(layoutInflater)
        initViewModel()
        observeResponse()
        setUpHospitals()
        val viewModel = ViewModelProvider(requireActivity())[LocationViewModel::class.java]
        viewModel.location.observe(viewLifecycleOwner) { (lat, lon) ->
            Log.d("HomeFragment", "Received lat: $lat, lon: $lon")
            this.lat = lat.toString()
            this.lon = lon.toString()
            getPharmacies()
        }
        return binding.root
    }

    private fun setUpHospitals() {
        pharmaciesAdapter = PharmaciesAdapter(pharmacies as ArrayList<MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
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
                val intent = Intent(requireContext(), PharmacyDetailsActivity::class.java)
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
        viewModel.response.observe(viewLifecycleOwner) { result ->
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

    private fun getPharmacies() {
        val userDetails = requireContext().getUserDetails()
        val request = PharmaciesRequest("", userDetails[User.ID]!!.toInt(), lon, userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), lat)
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchPharmacies(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMorePharmacies() {
        isLoading = true
        currentPage += 1
        pharmaciesAdapter.showLoadingFooter()
        getPharmacies()
    }
}