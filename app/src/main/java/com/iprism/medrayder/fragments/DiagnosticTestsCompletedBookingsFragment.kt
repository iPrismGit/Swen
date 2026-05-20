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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.medrayder.activities.DiagnosticTestBookingDetailsActivity
import com.iprism.medrayder.activities.HospitalMedicineBookingDetailsActivity
import com.iprism.medrayder.adapters.DiagnosticTestBookingsAdapter
import com.iprism.medrayder.databinding.FragmentHospitalMedicineObGoingBinding
import com.iprism.medrayder.interfaces.OnBookingItemClickListener
import com.iprism.medrayder.models.diagnostictestbookings.HistoryItem
import com.iprism.medrayder.models.labtestbookings.LabTestBookingsRequest
import com.iprism.medrayder.repository.DiagnosticsRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.DiagnosticTestsBookingsViewModel
import com.iprism.medrayder.viewmodels.LabTestsBookingsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class DiagnosticTestsCompletedBookingsFragment : Fragment() {

    private lateinit var binding: FragmentHospitalMedicineObGoingBinding
    private lateinit var viewModel: DiagnosticTestsBookingsViewModel
    private lateinit var labTestBookingsAdapter: DiagnosticTestBookingsAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var bookings = mutableListOf<HistoryItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHospitalMedicineObGoingBinding.inflate(layoutInflater)
        setUpBookings()
        initViewModel()
        observeResponse()
        fetchBookings()
        refreshBookings()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshBookings() {
        binding.refresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            currentPage = 1
            isLastPage = false
            bookings.clear()
            labTestBookingsAdapter.notifyDataSetChanged()
            fetchBookings()
            binding.refresh.isRefreshing = false
        })
    }

    private fun setUpBookings() {
        labTestBookingsAdapter = DiagnosticTestBookingsAdapter(bookings as ArrayList<HistoryItem?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.bookingsRv.apply {
            layoutManager = linearLayoutManager
            adapter = labTestBookingsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreBookings()
                        }
                    }
                }
            })
        }
        labTestBookingsAdapter.setOnBookingItemClickListener(object :
            OnBookingItemClickListener {
            override fun onItemClicked(id: String, bookingType: String) {
                val intent =
                    Intent(requireContext(), DiagnosticTestBookingDetailsActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("bookingType", bookingType)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = DiagnosticsRepository()
        val factory = ViewModelFactory { DiagnosticTestsBookingsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DiagnosticTestsBookingsViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.completedResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                        binding.noDataLl.visibility = View.GONE
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.noDataLl.visibility = View.GONE
                    isLoading = false
                    labTestBookingsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.history
                    if (newBookings.isNotEmpty()) {
                        bookings.addAll(newBookings)
                        labTestBookingsAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    labTestBookingsAdapter.removeLoadingFooter()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchBookings() {
        val userDetails = requireContext().getUserDetails()
        val request = LabTestBookingsRequest(
            userDetails[User.ID]!!.toInt(),
            userDetails[User.LANG].toString(),
            currentPage,
            userDetails[User.AUTH_TOKEN].toString()
        )
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchDiagnosticTestsCompletedBookings(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreBookings() {
        isLoading = true
        currentPage += 1
        labTestBookingsAdapter.showLoadingFooter()
        fetchBookings()
    }
}