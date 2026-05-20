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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.swen.activities.HospitalAdmissionBookingDetailsActivity
import com.iprism.swen.adapters.HospitalAdmissionBookingsAdapter
import com.iprism.swen.databinding.FragmentHospitalMedicineCompletedBinding
import com.iprism.swen.interfaces.OnHospitalAdmitBookingItemClickListener
import com.iprism.swen.models.hospitaladmitongoingbookings.HistoryItem
import com.iprism.swen.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.viewmodels.HospitalAdmissionBookingsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class HospitalAdmissionCompletedFragment : Fragment() {

    private lateinit var binding : FragmentHospitalMedicineCompletedBinding
    private lateinit var viewModel: HospitalAdmissionBookingsViewModel
    private lateinit var hospitalAdmissionBookingsAdapter: HospitalAdmissionBookingsAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var bookings = mutableListOf<HistoryItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHospitalMedicineCompletedBinding.inflate(layoutInflater)
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
            hospitalAdmissionBookingsAdapter.notifyDataSetChanged()
            fetchBookings()
            binding.refresh.isRefreshing = false
        })
    }

    private fun setUpBookings() {
        hospitalAdmissionBookingsAdapter = HospitalAdmissionBookingsAdapter(bookings as ArrayList<HistoryItem?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.bookingsRv.apply {
            layoutManager = linearLayoutManager
            adapter = hospitalAdmissionBookingsAdapter
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
        hospitalAdmissionBookingsAdapter.setOnBookingItemClickListener(object :
            OnHospitalAdmitBookingItemClickListener {
            override fun onItemClicked(id: String, bookingType : String) {
                val intent = Intent(requireContext(), HospitalAdmissionBookingDetailsActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }

            override fun onCallClicked(mobile: String) {
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalAdmissionBookingsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalAdmissionBookingsViewModel::class.java]
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
                    hospitalAdmissionBookingsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.history
                    if (newBookings.isNotEmpty()) {
                        bookings.addAll(newBookings)
                        hospitalAdmissionBookingsAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    hospitalAdmissionBookingsAdapter.removeLoadingFooter()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchBookings() {
        val userDetails = requireContext().getUserDetails()
        val request = HospitalAdmissionOnGoingRequest(userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchHospitalAdmissionCompletedBookings(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreBookings() {
        isLoading = true
        currentPage += 1
        hospitalAdmissionBookingsAdapter.showLoadingFooter()
        fetchBookings()
    }
}