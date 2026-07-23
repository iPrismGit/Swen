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
import com.iprism.swen.activities.SurgeonDoctorBookingDetailsActivity
import com.iprism.swen.adapters.SurgeonDoctorBookingsAdapter
import com.iprism.swen.databinding.FragmentAppointmentDoctorOnGoingBookingsBinding
import com.iprism.swen.interfaces.OnDoctorBookingItemClickListener
import com.iprism.swen.models.surgeondoctorbookings.HistoryItem
import com.iprism.swen.models.surgeondoctorbookings.SurgeonDoctorBookingsRequest
import com.iprism.swen.repository.SurgeonSymptomsDoctorRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.viewmodels.SurgeonDoctorBookingsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import kotlin.text.get

class SurgeonDoctorOnGoingBookingsFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentDoctorOnGoingBookingsBinding
    private lateinit var viewModel: SurgeonDoctorBookingsViewModel
    private lateinit var bookingsAdapter: SurgeonDoctorBookingsAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var bookings = mutableListOf<HistoryItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAppointmentDoctorOnGoingBookingsBinding.inflate(inflater, container, false)
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
            bookingsAdapter.notifyDataSetChanged()
            fetchBookings()
            binding.refresh.isRefreshing = false
        })
    }

    private fun setUpBookings() {
        bookingsAdapter = SurgeonDoctorBookingsAdapter(bookings as ArrayList<HistoryItem?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.bookingsRv.apply {
            layoutManager = linearLayoutManager
            adapter = bookingsAdapter
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
        bookingsAdapter.setOnDoctorItemClickListener(object : OnDoctorBookingItemClickListener {
            override fun onItemClicked(bookingId: String) {
                val intent = Intent(requireContext(), SurgeonDoctorBookingDetailsActivity::class.java)
                intent.putExtra("bookingId", bookingId)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = SurgeonSymptomsDoctorRepository()
        val factory = ViewModelFactory { SurgeonDoctorBookingsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[SurgeonDoctorBookingsViewModel::class.java]
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
                    bookingsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.history
                    if (newBookings.isNotEmpty()) {
                        bookings.addAll(newBookings)
                        bookingsAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    bookingsAdapter.removeLoadingFooter()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchBookings() {
        val userDetails = requireContext().getUserDetails()
        val request = SurgeonDoctorBookingsRequest(
            userDetails[User.ID]!!.toInt(),
            currentPage,
            userDetails[User.AUTH_TOKEN].toString(),
            Constants.MAIN_DATA_ID
        )
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchSurgeonDoctorsOngoingBookings(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreBookings() {
        isLoading = true
        currentPage += 1
        bookingsAdapter.showLoadingFooter()
        fetchBookings()
    }
}