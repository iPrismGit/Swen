package com.iprism.swen.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.adapters.HomeCareServiceCategoriesAdapter
import com.iprism.swen.databinding.ActivityHomeCareServiceSubCategoriesBinding
import com.iprism.swen.interfaces.OnServiceItemClickListener
import com.iprism.swen.models.homeservices.HomeServicesRequest
import com.iprism.swen.models.homeservices.ResponseItem
import com.iprism.swen.repository.HomeVisitServicesRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.HomeServicesCategoriesViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import kotlin.text.toInt

class HomeCareServiceSubCategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeCareServiceSubCategoriesBinding
    private lateinit var viewModel : HomeServicesCategoriesViewModel
    private var catId : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeCareServiceSubCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra("catId")) {
            catId = intent.getStringExtra("catId")!!
            binding.titleTxt.text = intent.getStringExtra("catName")!!
        }
        handleBack()
        initViewModel()
        observeResponse()
        fetchHomeServices()
    }

    private fun setupAdapter(items : ArrayList<ResponseItem>) {
        val adapter = HomeCareServiceCategoriesAdapter(items)
        val layoutManager = GridLayoutManager(this, 4)
        binding.categoriesRv.adapter = adapter
        binding.categoriesRv.layoutManager = layoutManager
        adapter.setupListener(object : OnServiceItemClickListener {
            override fun onItemClick(catId: Int, catName: String) {
            }
        })
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { p0 ->
            finish()
        }
    }

    private fun initViewModel() {
        val repository = HomeVisitServicesRepository()
        val factory = ViewModelFactory { HomeServicesCategoriesViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HomeServicesCategoriesViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    setupAdapter(result.data.response)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun fetchHomeServices() {
        val userDetails = getUserDetails()
        val request = HomeServicesRequest(
            userDetails[User.ID]!!.toInt(),
            catId.toInt(),
            "sub_categories",
            userDetails[User.AUTH_TOKEN]!!
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchHomeServices(req)
        }
        Log.d("requestLoading", request.toString())
    }
}