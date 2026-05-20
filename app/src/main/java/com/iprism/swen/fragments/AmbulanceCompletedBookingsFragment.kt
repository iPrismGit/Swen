package com.iprism.swen.fragments

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
import com.iprism.swen.R
import com.iprism.swen.activities.HospitalAmbulanceBookingDetailsActivity
import com.iprism.swen.adapters.HospitalAmbulanceBookingsAdapter
import com.iprism.swen.databinding.FragmentHospitalMedicineObGoingBinding
import com.iprism.swen.interfaces.OnAmbulanceBookingItemClickListener
import com.iprism.swen.models.hospitalambulancebookings.HistoryItem
import com.iprism.swen.models.labtestbookings.LabTestBookingsRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.HospitalAmbulanceBookingsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class AmbulanceCompletedBookingsFragment : Fragment() {

    private lateinit var binding : FragmentHospitalMedicineObGoingBinding
    private lateinit var viewModel: HospitalAmbulanceBookingsViewModel
    private lateinit var hospitalAmbulanceBookingsAdapter: HospitalAmbulanceBookingsAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var bookings = mutableListOf<HistoryItem>()
    private val CALL_PHONE_PERMISSION_CODE = 1
    private var mobile = ""

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
            hospitalAmbulanceBookingsAdapter.notifyDataSetChanged()
            fetchBookings()
            binding.refresh.isRefreshing = false
        })
    }

    private fun setUpBookings() {
        hospitalAmbulanceBookingsAdapter = HospitalAmbulanceBookingsAdapter(bookings as ArrayList<HistoryItem?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.bookingsRv.apply {
            layoutManager = linearLayoutManager
            adapter = hospitalAmbulanceBookingsAdapter
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
        hospitalAmbulanceBookingsAdapter.setOnBookingItemClickListener(object :
            OnAmbulanceBookingItemClickListener {
            override fun onItemClicked(details : HistoryItem) {
                val intent = Intent(requireContext(), HospitalAmbulanceBookingDetailsActivity::class.java)
                intent.putExtra("details", details)
                startActivity(intent)
            }

            override fun onCallClicked(mobile: String) {
                this@AmbulanceCompletedBookingsFragment.mobile = mobile
                makePhoneCall(mobile)
            }

            override fun onTrackClicked(details: HistoryItem) {
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalAmbulanceBookingsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalAmbulanceBookingsViewModel::class.java]
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
                    hospitalAmbulanceBookingsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.history
                    if (newBookings.isNotEmpty()) {
                        bookings.addAll(newBookings)
                        hospitalAmbulanceBookingsAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    hospitalAmbulanceBookingsAdapter.removeLoadingFooter()
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
            viewModel.fetchHospitalAmbulanceCompletedBookings(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreBookings() {
        isLoading = true
        currentPage += 1
        hospitalAmbulanceBookingsAdapter.showLoadingFooter()
        fetchBookings()
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
}