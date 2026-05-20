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
import com.iprism.swen.activities.LabTestBookingDetailsActivity
import com.iprism.swen.adapters.LabTestBookingsAdapter
import com.iprism.swen.databinding.FragmentHospitalMedicineObGoingBinding
import com.iprism.swen.interfaces.OnBookingItemClickListener
import com.iprism.swen.models.labtestbookings.HistoryItem
import com.iprism.swen.models.labtestbookings.LabTestBookingsRequest
import com.iprism.swen.repository.LabsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.viewmodels.LabTestsBookingsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class LabTestsCompletedBookingsFragment : Fragment() {

    private lateinit var binding : FragmentHospitalMedicineObGoingBinding
    private lateinit var viewModel: LabTestsBookingsViewModel
    private lateinit var labTestBookingsAdapter: LabTestBookingsAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var bookings = mutableListOf<HistoryItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        labTestBookingsAdapter = LabTestBookingsAdapter(bookings as ArrayList<HistoryItem?>)
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
            override fun onItemClicked(id: String, bookingType : String) {
                val intent = Intent(requireContext(), LabTestBookingDetailsActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("bookingType", bookingType)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = LabsRepository()
        val factory = ViewModelFactory { LabTestsBookingsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[LabTestsBookingsViewModel::class.java]
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
        val request = LabTestBookingsRequest(userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchLabTestsCompletedBookings(request)
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