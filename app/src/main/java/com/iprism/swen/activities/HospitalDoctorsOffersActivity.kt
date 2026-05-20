package com.iprism.swen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.swen.adapters.CouponsAdapter
import com.iprism.swen.databinding.ActivityOffersBinding
import com.iprism.swen.interfaces.OnCouponItemClickListener
import com.iprism.swen.models.onlinedoctorscoupons.CouponsItem
import com.iprism.swen.models.onlinedoctorscoupons.CouponRequest
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.HospitalDoctorCouponsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class HospitalDoctorsOffersActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOffersBinding
    private lateinit var viewModel: HospitalDoctorCouponsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOffersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBackImg()
        initViewModel()
        observeResponse()
        val userDetails = getUserDetails()
        val request = CouponRequest(userDetails[User.ID]!!, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getOnlineCoupons(req)
        }
        Log.d("request", request.toString())
    }

    private fun handleBackImg() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalDoctorCouponsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalDoctorCouponsViewModel::class.java]
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