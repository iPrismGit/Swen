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
import com.iprism.swen.adapters.DiagnosticCentersAdapter
import com.iprism.swen.databinding.ActivityDiagnosticSearchBinding
import com.iprism.swen.interfaces.OnDiagnosticItemClickListener
import com.iprism.swen.models.dignosticcenters.DiagnosticCentersRequest
import com.iprism.swen.repository.DiagnosticsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.viewmodels.DiagnosticsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class DiagnosticSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiagnosticSearchBinding
    private lateinit var viewModel: DiagnosticsViewModel
    private lateinit var diagnosticCentersAdapter: DiagnosticCentersAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var diagnosticCenters = mutableListOf<com.iprism.swen.models.dignosticcenters.MainDataItem>()
    var lat = ""
    var lon = ""
    var search = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagnosticSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("lat")) {
            lat = intent.getStringExtra("lat")!!
            lon = intent.getStringExtra("lon")!!
            search = intent.getStringExtra("search")!!
            binding.searchEt.setText(search)
        }
        setUpDiagnosticCenters()
        initViewModel()
        observeResponse()
        if (search.isNotEmpty()) {
            fetchDiagnosticCenters(binding.searchEt.text.toString().trim())
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
                diagnosticCenters.clear()
                fetchDiagnosticCenters(binding.searchEt.text.toString().trim())
            }
            true
        }
    }

    private fun setUpDiagnosticCenters() {
        diagnosticCentersAdapter = DiagnosticCentersAdapter(diagnosticCenters as ArrayList<com.iprism.swen.models.dignosticcenters.MainDataItem?>)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.diagnosticsRv.apply {
            layoutManager = linearLayoutManager
            adapter = diagnosticCentersAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreDiagnosticCenters()
                        }
                    }
                }
            })
        }
        diagnosticCentersAdapter.setOnDoctorItemClickListener(object : OnDiagnosticItemClickListener {
            override fun onItemClicked(id: String, name : String) {
                val intent = Intent(this@DiagnosticSearchActivity, DiagnosticCenterDetailsActivity::class.java)
                intent.putExtra("diagnosticId", id)
                intent.putExtra("diagnosticName", name)
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = DiagnosticsRepository()
        val factory = ViewModelFactory { DiagnosticsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DiagnosticsViewModel::class.java]
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
                    diagnosticCentersAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.mainData
                    if (newBookings.isNotEmpty()) {
                        diagnosticCenters.addAll(newBookings)
                        diagnosticCentersAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    isLoading = false
                    diagnosticCentersAdapter.removeLoadingFooter()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchDiagnosticCenters(search : String) {
        val userDetails = getUserDetails()
        val request = DiagnosticCentersRequest(search, userDetails[User.ID]!!.toInt(), lon, userDetails[User.LANG].toString(), currentPage, userDetails[User.AUTH_TOKEN].toString(), lat)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchDiagnosticCenters(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreDiagnosticCenters() {
        isLoading = true
        currentPage += 1
        diagnosticCentersAdapter.showLoadingFooter()
        fetchDiagnosticCenters(binding.searchEt.text.toString().trim())
    }
}