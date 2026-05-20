package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.ActivityEcardBinding
import com.iprism.swen.models.ecard.ECardRequest
import com.iprism.swen.repository.SubscriptionRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.ECardViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import kotlin.text.toInt

class ECardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEcardBinding
    private lateinit var viewModel: ECardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEcardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViewModel()
        observeResponse()
        fetchECard()
        handleBack()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val repository = SubscriptionRepository()
        val factory = ViewModelFactory { ECardViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[ECardViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.validTillDateTxt.text = "Valid Till Date : ${result.data.response.toDate}"
                    binding.subscriptionIdTxt.text = "Subscription Id : ${result.data.response.uniqueId}"
                    binding.nameTxt.text = result.data.response.name
                    binding.cardCl.visibility = View.VISIBLE
                    if (result.data.response.image.isNotEmpty()) {
                        Glide.with(this)
                            .load(Constants.IMAGES_BASE_URL + result.data.response.image)
                            .into(binding.profileImg)
                    }
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun fetchECard() {
        val userDetails = getUserDetails()
        val request = ECardRequest(userDetails[User.ID]!!.toInt(), userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchECard(req)
        }
        Log.d("requestLoading", request.toString())
    }
}