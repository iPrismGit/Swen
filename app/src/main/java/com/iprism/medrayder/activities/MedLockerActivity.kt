package com.iprism.medrayder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.medrayder.adapters.MedLockerAdapter
import com.iprism.medrayder.databinding.ActivityMedLovkerBinding
import com.iprism.medrayder.interfaces.OnDocumentItemClickListener
import com.iprism.medrayder.interfaces.OnFamilyItemClickListener
import com.iprism.medrayder.models.medlocker.MedLockerItem
import com.iprism.medrayder.models.medlocker.MedLockerRequest
import com.iprism.medrayder.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.MedLockerViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class MedLockerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMedLovkerBinding
    private lateinit var viewModel: MedLockerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedLovkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleAddDocumentsTxt()
        initViewModel()
        observeResponse()
    }

    override fun onResume() {
        super.onResume()
        fetchMedLocker()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleAddDocumentsTxt() {
        binding.addDocumentsTxt.setOnClickListener(View.OnClickListener {
            val intent =  Intent(this, AddMedLockerDocumentsActivity::class.java)
            startActivity(intent)
        })
    }

    private fun initViewModel() {
        val repository = CommonRepository()
        val factory = ViewModelFactory { MedLockerViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[MedLockerViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.noDataLl.visibility = View.GONE
                    binding.progress.hideProgress()
                    setupMedLocker(result.data.response.medLocker)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupMedLocker(items : List<MedLockerItem>) {
        val medLockerAdapter = MedLockerAdapter(items)
        binding.medLockerRv.layoutManager = LinearLayoutManager(this)
        binding.medLockerRv.adapter = medLockerAdapter
        medLockerAdapter.setOnDocumentItemClickListener(object : OnDocumentItemClickListener {
            override fun onItemClicked(medLockerItem: MedLockerItem) {
                if (!medLockerItem.images[0].image.endsWith(".pdf")) {
                    val intent = Intent(this@MedLockerActivity, ViewDocumentsActivity::class.java)
                    val imageList = ArrayList<String>()
                    for (item in medLockerItem.images) {
                        imageList.add(item.image)
                    }
                    intent.putStringArrayListExtra("images", imageList)
                    intent.putExtra("name", medLockerItem.name)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@MedLockerActivity, PdfViewActivity::class.java)
                    intent.putExtra("pdfUrl", medLockerItem.images[0].image)
                    intent.putExtra("name", medLockerItem.name)
                    startActivity(intent)
                }
            }
        })
    }

    private fun fetchMedLocker() {
        val userDetails = getUserDetails()
        val request = MedLockerRequest(emptyList(), userDetails[User.ID]!!.toInt(), "", "view", "", userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.medLocker(req)
        }
        Log.d("requestLoading", request.toString())
    }
}