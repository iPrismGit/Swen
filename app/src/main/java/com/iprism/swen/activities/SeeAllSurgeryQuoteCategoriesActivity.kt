package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.models.allsurgeryquotes.AllSurgeryQuotesRequest
import com.iprism.swen.models.allsurgeryquotes.ResponseItem
import com.iprism.swen.adapters.AllSurgicalQuoteCategoriesAdapter
import com.iprism.swen.databinding.ActivityAllSurgeonSymptomDoctorsBinding
import com.iprism.swen.interfaces.OnDoctorWithSymptomsSpecialityClickListener
import com.iprism.swen.repository.SurgicalQuoteRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.SurgicalQuoteCategoriesViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import kotlin.text.toInt

class SeeAllSurgeryQuoteCategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllSurgeonSymptomDoctorsBinding
    private lateinit var viewModel : SurgicalQuoteCategoriesViewModel
    var lat = ""
    var lon = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllSurgeonSymptomDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra("lat")) {
            lat = intent.getStringExtra("lat")!!
            lon = intent.getStringExtra("lon")!!
        }
        binding.catNameTxt.text = "Get a Surgical Quote"
        handleBack()
        initViewModel()
        observeResponse()
        fetchAllSurgeonSymptoms()
    }

    private fun setupSymptoms(symptoms : ArrayList<ResponseItem>) {
        val allSymptomsAdapter = AllSurgicalQuoteCategoriesAdapter(symptoms)
        val linearLayoutManager = GridLayoutManager(this, 4)
        binding.allTypesDoctorsRv.layoutManager = linearLayoutManager
        binding.allTypesDoctorsRv.adapter = allSymptomsAdapter
        allSymptomsAdapter.setupListener(object : OnDoctorWithSymptomsSpecialityClickListener {
            override fun onItemClicked(catId: String, catName: String) {
                val intent = Intent(this@SeeAllSurgeryQuoteCategoriesActivity, SurgerySymptomHospitalsActivity::class.java)
                intent.putExtra("symptomId", catId)
                intent.putExtra("catName", catName)
                intent.putExtra("lat", lat)
                intent.putExtra("lon", lon)
                startActivity(intent)
            }
        })
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    private fun initViewModel() {
        val repository = SurgicalQuoteRepository()
        val factory = ViewModelFactory { SurgicalQuoteCategoriesViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[SurgicalQuoteCategoriesViewModel::class.java]
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
                    setupSymptoms(result.data.response)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun fetchAllSurgeonSymptoms() {
        val userDetails = getUserDetails()
        val request = AllSurgeryQuotesRequest(
            userDetails[User.ID]!!.toInt(),
            userDetails[User.AUTH_TOKEN].toString(),
            Constants.MAIN_DATA_ID
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchAllSurgicalQuoteCategories(req)
        }
        Log.d("requestLoading", request.toString())
    }
}