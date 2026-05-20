package com.iprism.medrayder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.medrayder.databinding.ActivityContentPagesBinding
import com.iprism.medrayder.models.contentpages.ContentPagesRequest
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.ContentPagesViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class ContentPagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentPagesBinding
    private lateinit var viewModel: ContentPagesViewModel
    private var tag : String = ""
    private var name : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tag = intent.getStringExtra("tag").toString()
        name = intent.getStringExtra("name").toString()
        binding.pageNameTxt.text = name
        handleBack()
        initViewModel()
        observeResponse()
        fetchContentPage()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val repository = CommonRepository()
        val factory = ViewModelFactory { ContentPagesViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[ContentPagesViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (tag.equals("about_us", true)) {
                        binding.contentTxt.text = result.data.response.aboutUs
                    } else if (tag.equals("privacy", true)) {
                        binding.contentTxt.text = result.data.response.privacy
                    } else if (tag.equals("terms", true)) {
                        binding.contentTxt.text = result.data.response.terms
                    } else if (tag.equals("refund", true)) {
                        binding.contentTxt.text = result.data.response.refund
                    }
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun fetchContentPage() {
        val addressListRequest = ContentPagesRequest(tag, getUserDetails()[User.LANG].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, addressListRequest) { req ->
            viewModel.fetchContentPage(req)
        }
        Log.d("requestLoading", addressListRequest.toString())
    }
}