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
import com.iprism.medrayder.activities.HospitalDoctorBookingDetailsActivity
import com.iprism.medrayder.activities.HospitalMedicineBookingDetailsActivity
import com.iprism.medrayder.activities.OnlineDoctorBookingDetailsActivity
import com.iprism.medrayder.adapters.HospitalMedicineBookingsAdapter
import com.iprism.medrayder.adapters.OnlineDoctorBookingsAdapter
import com.iprism.medrayder.databinding.FragmentPharmacyOnGoingBinding
import com.iprism.medrayder.interfaces.OnBookingItemClickListener
import com.iprism.medrayder.interfaces.OnOnlineDoctorBookingItemClickListener
import com.iprism.medrayder.models.hospitalmedicneongoing.BookingsItem
import com.iprism.medrayder.models.onlinedoctorbookinghistory.HistoryItem
import com.iprism.medrayder.models.onlinedoctorbookinghistory.OnlineDoctorBookingHistoryRequest
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.repository.OnlineDoctorRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.HospitalDoctorBookingsViewModel
import com.iprism.medrayder.viewmodels.OnlineDoctorBookingsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class HospitalDoctorOnGoingBookingsFragment : Fragment() {

    private lateinit var binding : FragmentPharmacyOnGoingBinding
    private lateinit var viewModel: HospitalDoctorBookingsViewModel
    private lateinit var onlineDoctorBookingsAdapter: OnlineDoctorBookingsAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var bookings = mutableListOf<HistoryItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPharmacyOnGoingBinding.inflate(layoutInflater)
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
            onlineDoctorBookingsAdapter.notifyDataSetChanged()
            fetchBookings()
            binding.refresh.isRefreshing = false
        })
    }

    private fun setUpBookings() {
        onlineDoctorBookingsAdapter = OnlineDoctorBookingsAdapter(bookings as ArrayList<HistoryItem?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.bookingsRv.apply {
            layoutManager = linearLayoutManager
            adapter = onlineDoctorBookingsAdapter
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
        onlineDoctorBookingsAdapter.setOnDoctorItemClickListener(object :
            OnOnlineDoctorBookingItemClickListener {
            override fun onItemClicked(bookingId : String) {
                val intent = Intent(requireContext(), HospitalDoctorBookingDetailsActivity::class.java)
                intent.putExtra("bookingId", bookingId)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalDoctorBookingsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalDoctorBookingsViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(viewLifecycleOwner) { result ->
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
                    onlineDoctorBookingsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.history
                    if (newBookings.isNotEmpty()) {
                        bookings.addAll(newBookings)
                        onlineDoctorBookingsAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    onlineDoctorBookingsAdapter.removeLoadingFooter()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchBookings() {
        val userDetails = requireContext().getUserDetails()
        val request = OnlineDoctorBookingHistoryRequest(
            userDetails[User.ID]!!.toInt(),
            userDetails[User.LANG].toString(),
            currentPage,
            userDetails[User.AUTH_TOKEN].toString()
        )
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.getHospitalDoctorBookingsHistory(req)
        }
        Log.d("requestBookings", request.toString())
    }

    private fun loadMoreBookings() {
        isLoading = true
        currentPage += 1
        onlineDoctorBookingsAdapter.showLoadingFooter()
        fetchBookings()
    }
}