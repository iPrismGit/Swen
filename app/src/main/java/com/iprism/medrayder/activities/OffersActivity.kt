package com.iprism.medrayder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.medrayder.adapters.CouponsAdapter
import com.iprism.medrayder.databinding.ActivityOffersBinding
import com.iprism.medrayder.interfaces.OnCouponItemClickListener
import com.iprism.medrayder.models.onlinedoctorscoupons.CouponsItem
import com.iprism.medrayder.models.onlinedoctorscoupons.CouponRequest
import com.iprism.medrayder.repository.CouponsRepository
import com.iprism.medrayder.repository.OnlineDoctorRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.CouponsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class OffersActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOffersBinding
    private lateinit var viewModel: CouponsViewModel
    private var tag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOffersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBackImg()
        initViewModel()
        observeResponse()
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag")!!
        }
        if (tag.equals("onlineDoctor", true)) {
            val userDetails = getUserDetails()
            val request = CouponRequest(userDetails[User.ID]!!, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.getOnlineCoupons(req)
            }
            Log.d("request", request.toString())
        } else if (tag.equals("diagnostic", true)) {
            val userDetails = getUserDetails()
            val request = CouponRequest(userDetails[User.ID]!!, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.fetchDiagnosticCoupons(req)
            }
            Log.d("request", request.toString())
        } else if (tag.equals("pharmacy", true)) {
            val userDetails = getUserDetails()
            val request = CouponRequest(userDetails[User.ID]!!, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.getPharmacyCoupons(req)
            }
            Log.d("request", request.toString())
        }  else if (tag.equals("lab", true)) {
            val userDetails = getUserDetails()
            val request = CouponRequest(userDetails[User.ID]!!, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.fetchLabCoupons(req)
            }
            Log.d("request", request.toString())
        } else if (tag.equals("hospitalMedicine", true)) {
            val userDetails = getUserDetails()
            val request = CouponRequest(userDetails[User.ID]!!, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.fetchMedicineCoupons(req)
            }
            Log.d("request", request.toString())
        } else if (tag.equals("hospitalDiagnostic", true)) {
            val userDetails = getUserDetails()
            val request = CouponRequest(userDetails[User.ID]!!, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
            NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
                viewModel.fetchHospitalDiagnosticCoupons(req)
            }
            Log.d("request", request.toString())
        }
    }

    private fun handleBackImg() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val repository = CouponsRepository()
        val factory = ViewModelFactory { CouponsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[CouponsViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.coupons.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    setupCoupons(result.data)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupCoupons(coupons : List<CouponsItem>) {
        val couponsAdapter = CouponsAdapter(coupons)
        binding.couponsRv.layoutManager = LinearLayoutManager(this)
        binding.couponsRv.adapter = couponsAdapter
        couponsAdapter.setOnDoctorItemClickListener(object : OnCouponItemClickListener{
            override fun onItemClicked(coupon: CouponsItem) {
                val resultIntent = Intent()
                resultIntent.putExtra("coupon", coupon)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        })
    }
}