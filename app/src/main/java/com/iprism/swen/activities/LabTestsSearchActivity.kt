package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.adapters.LabTestCentersAdapter
import com.iprism.swen.databinding.ActivityLabTestsSearchBinding
import com.iprism.swen.interfaces.OnDiagnosticItemClickListener
import com.iprism.swen.models.labcenters.LabCentersRequest
import com.iprism.swen.models.labcenters.MainDataItem
import com.iprism.swen.repository.LabsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.viewmodels.LabsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class LabTestsSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLabTestsSearchBinding
    private lateinit var viewModel: LabsViewModel
    private lateinit var labsAdapter: LabTestCentersAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var labs = mutableListOf<MainDataItem>()
    var lat = ""
    var lon = ""
    var search = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLabTestsSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("lat")) {
            lat = intent.getStringExtra("lat")!!
            lon = intent.getStringExtra("lon")!!
            search = intent.getStringExtra("search")!!
            binding.searchEt.setText(search)
        }
        setUpLabCenters()
        initViewModel()
        observeResponse()
        if (search.isNotEmpty()) {
            fetchLabCenters(binding.searchEt.text.toString())
        } else {
            showKeyboard()
        }
        handleSearchEt()
        handleBackImg()
    }

    private fun handleBackImg() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showKeyboard() {
        binding.searchEt.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun handleSearchEt() {
        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.hideSoftInputFromWindow(binding.mainCl.windowToken, 0)
                currentPage = 1
                isLastPage = false
                labs.clear()
                fetchLabCenters(binding.searchEt.text.toString().trim())
            }
            true
        }
    }

    private fun setUpLabCenters() {
        labsAdapter= LabTestCentersAdapter(labs as ArrayList<MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(this)
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
                val intent = Intent(this@LabTestsSearchActivity, LabDetailsActivity::class.java)
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
        viewModel.response.observe(this) { result ->
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

    private fun fetchLabCenters(search : String) {
        val userDetails = getUserDetails()
        val request = LabCentersRequest(search, userDetails[User.ID]!!.toInt(), lon, userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), lat)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchLabs(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreLabCenters() {
        isLoading = true
        currentPage += 1
        labsAdapter.showLoadingFooter()
        fetchLabCenters(binding.searchEt.text.toString().trim())
    }
}