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
import com.iprism.swen.activities.HospitalDetailsActivity
import com.iprism.swen.adapters.FilterCatsAdapter
import com.iprism.swen.adapters.FilterSubCatsAdapter
import com.iprism.swen.adapters.HospitalsAdapter
import com.iprism.swen.databinding.FragmentHospitalBinding
import com.iprism.swen.interfaces.OnFilterCatItemClickListener
import com.iprism.swen.interfaces.OnFilterSpecialityItemActionListener
import com.iprism.swen.interfaces.OnHospitalItemClickListener
import com.iprism.swen.models.filters.DetailsItem
import com.iprism.swen.models.filters.FiltersItem
import com.iprism.swen.models.filters.FiltersRequest
import com.iprism.swen.models.filters.SpecialitiesItem
import com.iprism.swen.models.hospitals.HospitalsRequest
import com.iprism.swen.models.hospitals.MainDataItem
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.HospitalsViewModel
import com.iprism.swen.viewmodels.LocationViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class HospitalFragment : Fragment() {

    private lateinit var binding : FragmentHospitalBinding
    private lateinit var viewModel: HospitalsViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var hospitals = mutableListOf<MainDataItem>()
    private var details : List<DetailsItem>? = null
    private lateinit var hospitalsAdapter: HospitalsAdapter
    private var filterSubCatsAdapter = FilterSubCatsAdapter()
    var lat = ""
    var lon = ""
    private var catPosition: Int = 0
    private var activeFilters: List<FiltersItem> = emptyList()
    private var isFilterApplied = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHospitalBinding.inflate(layoutInflater)
        handleFilterLL()
        handleBackImg()
        initViewModel()
        observeResponse()
        observeFilterResponse()
        setUpHospitals()
        val viewModel = ViewModelProvider(requireActivity())[LocationViewModel::class.java]
        viewModel.location.observe(viewLifecycleOwner) { (lat, lon) ->
            Log.d("HomeFragment", "Received lat: $lat, lon: $lon")
            this.lat = lat.toString()
            this.lon = lon.toString()
            getHospitals("view", emptyList())
        }
        handleApplyFilterLL()
        resetAllFilters()
        return binding.root
    }

    private fun setUpHospitals() {
        hospitalsAdapter = HospitalsAdapter(hospitals as ArrayList<MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.hospitalsRv.apply {
            layoutManager = linearLayoutManager
            adapter = hospitalsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreDoctors()
                        }
                    }
                }
            })
        }
        hospitalsAdapter.setOnDoctorItemClickListener(object : OnHospitalItemClickListener{
            override fun onItemClicked(hospitalId: String) {
                val intent = Intent(requireContext(), HospitalDetailsActivity::class.java)
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                startActivity(intent)
            }
        })
    }

    private fun handleFilterLL() {
        binding.filterLl.setOnClickListener(View.OnClickListener {
            if (details == null) {
                getFilters()
            }
            binding.filterLlView.visibility = View.VISIBLE
            binding.mainCl.visibility = View.GONE
        })
    }

    private fun handleApplyFilterLL() {
        binding.applyFilterBtn.setOnClickListener(View.OnClickListener {
            if (buildFilterList().isEmpty()) {
                requireContext().showToast("Please Select Any One Filter")
            } else {
                currentPage = 1
                isLastPage = false
                hospitals.clear()
                hospitalsAdapter.notifyDataSetChanged()
                val filters = buildFilterList()
                activeFilters = filters
                isFilterApplied = filters.isNotEmpty()
                Log.d("FiltersPayload", activeFilters.toString())
                binding.filterLlView.visibility = View.GONE
                binding.mainCl.visibility = View.VISIBLE
                getHospitals("filter", filters)
            }
        })
    }

    private fun resetAllFilters() {
        binding.resetBtn.setOnClickListener(View.OnClickListener {
            details?.forEach { category ->
                category.specialities.forEach { speciality ->
                    speciality.status = false
                }
            }
            filterSubCatsAdapter.setFilterValues(details!![catPosition].specialities)
            currentPage = 1
            isLastPage = false
            hospitals.clear()
            hospitalsAdapter.notifyDataSetChanged()
            activeFilters = emptyList()
            isFilterApplied = false
            binding.filterLlView.visibility = View.GONE
            binding.mainCl.visibility = View.VISIBLE
            getHospitals("view", emptyList())
        })
    }

    private fun setUpFilterCats(cats : List<DetailsItem>) {
        val filterCatsAdapter = FilterCatsAdapter(cats)
        binding.filterCatsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.filterCatsRv.adapter = filterCatsAdapter
        filterCatsAdapter.setListener(object : OnFilterCatItemClickListener{
            override fun onItemClicked(position: String) {
                if (details!![position.toInt()].specialities.isNotEmpty()) {
                    binding.filterSubCatsRv.visibility = View.VISIBLE
                    binding.noFilterDataLl.visibility = View.GONE
                    filterSubCatsAdapter.setFilterValues(details!![position.toInt()].specialities)
                } else {
                    binding.filterSubCatsRv.visibility = View.GONE
                    binding.noFilterDataLl.visibility = View.VISIBLE
                }
                catPosition = position.toInt()
            }
        })
    }

    private fun setUpFilterSubCats() {
        filterSubCatsAdapter = FilterSubCatsAdapter()
        binding.filterSubCatsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.filterSubCatsRv.adapter = filterSubCatsAdapter
        filterSubCatsAdapter.setListener(object : OnFilterSpecialityItemActionListener{
            override fun onFilterValueClicked(position: Int, item: SpecialitiesItem) {
                storeFilterResponse(position, item)
            }
        })
    }

    private fun buildFilterList(): ArrayList<FiltersItem> {
        val filterList = ArrayList<FiltersItem>()

        details?.forEach { category ->
            val selectedSpecialities = category.specialities
                .filter { it.status }
                .mapNotNull { it.specialityId }

            if (selectedSpecialities.isNotEmpty()) {
                filterList.add(FiltersItem(category.categoryId, selectedSpecialities))
            }
        }

        return filterList
    }

    private fun storeFilterResponse(position: Int?, filterResponse: SpecialitiesItem?) {
        details!![catPosition].specialities[position!!] = filterResponse!!
        details!![catPosition].specialities[position].status = filterResponse.status
    }

    private fun handleBackImg() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            binding.filterLlView.visibility = View.GONE
            binding.mainCl.visibility = View.VISIBLE
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalsViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                        binding.noDataLl.visibility = View.GONE
                    }
                }

                is UiState.Success -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.noDataLl.visibility = View.GONE
                    isLoading = false
                    hospitalsAdapter.removeLoadingFooter()
                    val newBookings = result.data.hospitalResponse.mainData
                    if (newBookings.isNotEmpty()) {
                        hospitals.addAll(newBookings)
                        hospitalsAdapter.notifyDataSetChanged()
                        if (result.data.hospitalResponse.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    isLoading = false
                    hospitalsAdapter.removeLoadingFooter()
                    //requireContext().showToast(result.message)
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeFilterResponse() {
        viewModel.filterResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    details = result.data.response.details
                    setUpFilterCats(details!!)
                    setUpFilterSubCats()
                    filterSubCatsAdapter.setFilterValues(details!![0].specialities)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun getHospitals(viewType : String, filters : List<FiltersItem>) {
        val userDetails = requireContext().getUserDetails()
        val request = HospitalsRequest("", userDetails[User.ID]!!.toInt(), lon, userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), lat, viewType, filters)
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.getHospitals(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun getFilters() {
        val userDetails = requireContext().getUserDetails()
        val request = FiltersRequest(userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.fetchFilters(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreDoctors() {
        isLoading = true
        currentPage += 1
        hospitalsAdapter.showLoadingFooter()
        if (isFilterApplied) {
            getHospitals("filter", activeFilters)
        } else {
            getHospitals("view", emptyList())
        }
    }
}