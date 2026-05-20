package com.iprism.medrayder.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.medrayder.R
import com.iprism.medrayder.activities.HospitalAdmissionBookingDetailsActivity
import com.iprism.medrayder.activities.HospitalMedicineBookingDetailsActivity
import com.iprism.medrayder.adapters.HospitalAdmissionBookingsAdapter
import com.iprism.medrayder.databinding.FragmentHospitalMedicineObGoingBinding
import com.iprism.medrayder.interfaces.OnBookingItemClickListener
import com.iprism.medrayder.interfaces.OnHospitalAdmitBookingItemClickListener
import com.iprism.medrayder.models.hospitaladmitongoingbookings.HistoryItem
import com.iprism.medrayder.models.hospitaladmitongoingbookings.HospitalAdmissionOnGoingRequest
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.HospitalAdmissionBookingsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class HospitalAdmissionOnGoingFragment : Fragment() {

    private lateinit var binding : FragmentHospitalMedicineObGoingBinding
    private lateinit var viewModel: HospitalAdmissionBookingsViewModel
    private lateinit var hospitalAdmissionBookingsAdapter: HospitalAdmissionBookingsAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var mobile = ""
    private var bookings = mutableListOf<HistoryItem>()
    private val CALL_PHONE_PERMISSION_CODE = 1

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
                this@HospitalAdmissionOnGoingFragment.mobile = mobile
                makePhoneCall(mobile)
            }
        })
    }

    private fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                CALL_PHONE_PERMISSION_CODE
            )
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALL_PHONE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(mobile)
            } else {
                requireContext().showToast(getString(R.string.permissission_denied_make_calls))
            }
        }
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalAdmissionBookingsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalAdmissionBookingsViewModel::class.java]
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
            viewModel.fetchHospitalAdmissionOngoingBookings(req)
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