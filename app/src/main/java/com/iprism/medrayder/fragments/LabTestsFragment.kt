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
import com.iprism.medrayder.activities.LabDetailsActivity
import com.iprism.medrayder.adapters.LabTestCentersAdapter
import com.iprism.medrayder.databinding.FragmentLabTestsBinding
import com.iprism.medrayder.interfaces.OnDiagnosticItemClickListener
import com.iprism.medrayder.models.labcenters.LabCentersRequest
import com.iprism.medrayder.models.labcenters.MainDataItem
import com.iprism.medrayder.repository.LabsRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.LabsViewModel
import com.iprism.medrayder.viewmodels.LocationViewModel
import com.iprism.medrayder.viewmodels.SearchTextViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class LabTestsFragment : Fragment() {

    private lateinit var binding: FragmentLabTestsBinding
    private lateinit var viewModel: LabsViewModel
    private lateinit var labsAdapter: LabTestCentersAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var labs = mutableListOf<MainDataItem>()
    var lat = ""
    var lon = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLabTestsBinding.inflate(layoutInflater)
        setUpLabCenters()
        initViewModel()
        observeResponse()
        val viewModel = ViewModelProvider(requireActivity())[LocationViewModel::class.java]
        viewModel.location.observe(viewLifecycleOwner) { (lat, lon) ->
            Log.d("HomeFragment", "Received lat: $lat, lon: $lon")
            this.lat = lat.toString()
            this.lon = lon.toString()
            fetchLabCenters()
        }
        return binding.root
    }

    private fun setUpLabCenters() {
        labsAdapter= LabTestCentersAdapter(labs as ArrayList<MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.labTestCentersRv.apply {
            layoutManager = linearLayoutManager
            adapter = labsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreLabCenters()
                        }
                    }
                }
            })
        }
        labsAdapter.setOnLabClickListener(object :
            OnDiagnosticItemClickListener {
            override fun onItemClicked(id: String, name : String) {
                val intent = Intent(requireContext(), LabDetailsActivity::class.java)
                intent.putExtra("labId", id)
                intent.putExtra("labName", name)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = LabsRepository()
        val factory = ViewModelFactory { LabsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[LabsViewModel::class.java]
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
                    labsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.mainData
                    if (newBookings.isNotEmpty()) {
                        labs.addAll(newBookings)
                        labsAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    isLoading = false
                    labsAdapter.removeLoadingFooter()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchLabCenters() {
        val userDetails = requireContext().getUserDetails()
        val request = LabCentersRequest("", userDetails[User.ID]!!.toInt(), lon, userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), lat)
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchLabs(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreLabCenters() {
        isLoading = true
        currentPage += 1
        labsAdapter.showLoadingFooter()
        fetchLabCenters()
    }
}