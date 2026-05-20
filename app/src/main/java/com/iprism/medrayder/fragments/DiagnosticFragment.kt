package com.iprism.medrayder.fragments

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
import com.iprism.medrayder.activities.DiagnosticCenterDetailsActivity
import com.iprism.medrayder.adapters.DiagnosticCentersAdapter
import com.iprism.medrayder.databinding.FragmentDiagnosticBinding
import com.iprism.medrayder.interfaces.OnDiagnosticItemClickListener
import com.iprism.medrayder.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.medrayder.models.dignosticcenters.MainDataItem
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.DiagnosticsViewModel
import com.iprism.medrayder.viewmodels.LocationViewModel
import com.iprism.medrayder.viewmodels.SearchTextViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class DiagnosticFragment : Fragment() {

    private lateinit var binding: FragmentDiagnosticBinding
    private lateinit var viewModel: DiagnosticsViewModel
    private lateinit var diagnosticCentersAdapter: DiagnosticCentersAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var diagnosticCenters = mutableListOf<MainDataItem>()
    var lat = ""
    var lon = ""

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDiagnosticBinding.inflate(layoutInflater)
        setUpDiagnosticCenters()
        initViewModel()
        observeResponse()
        val viewModel = ViewModelProvider(requireActivity())[LocationViewModel::class.java]
        viewModel.location.observe(viewLifecycleOwner) { (lat, lon) ->
            Log.d("HomeFragment", "Received lat: $lat, lon: $lon")
            this.lat = lat.toString()
            this.lon = lon.toString()
            fetchDiagnosticCenters()
        }
        return binding.root
    }

    private fun setUpDiagnosticCenters() {
        diagnosticCentersAdapter = DiagnosticCentersAdapter(diagnosticCenters as ArrayList<MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.diagnosticCentersRv.apply {
            layoutManager = linearLayoutManager
            adapter = diagnosticCentersAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreDiagnosticCenters()
                        }
                    }
                }
            })
        }
        diagnosticCentersAdapter.setOnDoctorItemClickListener(object : OnDiagnosticItemClickListener {
            override fun onItemClicked(id: String, name : String) {
                val intent = Intent(requireContext(), DiagnosticCenterDetailsActivity::class.java)
                intent.putExtra("diagnosticId", id)
                intent.putExtra("diagnosticName", name)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = DiagnosticsRepository()
        val factory = ViewModelFactory { DiagnosticsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DiagnosticsViewModel::class.java]
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
                    diagnosticCentersAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.mainData
                    if (newBookings.isNotEmpty()) {
                        diagnosticCenters.addAll(newBookings)
                        diagnosticCentersAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    isLoading = false
                    diagnosticCentersAdapter.removeLoadingFooter()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchDiagnosticCenters() {
        val userDetails = requireContext().getUserDetails()
        val request = DiagnosticCentersRequest("", userDetails[User.ID]!!.toInt(), lon, userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), lat)
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchDiagnosticCenters(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreDiagnosticCenters() {
        isLoading = true
        currentPage += 1
        diagnosticCentersAdapter.showLoadingFooter()
        fetchDiagnosticCenters()
    }
}