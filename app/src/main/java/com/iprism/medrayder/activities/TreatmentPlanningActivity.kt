package com.iprism.medrayder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.medrayder.adapters.TreatmentPlaningAdapter
import com.iprism.medrayder.databinding.ActivityTreatmentPlanningBinding
import com.iprism.medrayder.interfaces.OnTreatmentPlaningItemClickListener
import com.iprism.medrayder.models.treatmentplaning.TreatmentPlaningRequest
import com.iprism.medrayder.models.treatmentplaning.TreatmentPlanningItem
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.TreatmentPlanningViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class TreatmentPlanningActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTreatmentPlanningBinding
    private lateinit var viewModel: TreatmentPlanningViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreatmentPlanningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleAddPlanTxt()
        initViewModel()
        observeResponse()

    }

    override fun onResume() {
        super.onResume()
        fetchTreatmentPlanning()
    }


    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleAddPlanTxt() {
        binding.addPlanTxt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, AddTreatmentPlanningActivity::class.java))
        })
    }

    private fun initViewModel() {
        val repository = CommonRepository()
        val factory = ViewModelFactory { TreatmentPlanningViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[TreatmentPlanningViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.noDataLl.visibility = View.GONE
                    setupTreatmentPlanning(result.data.response.treatmentPlanning)
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

    private fun setupTreatmentPlanning(items : List<TreatmentPlanningItem>) {
        val medLockerAdapter = TreatmentPlaningAdapter(items)
        binding.treatmentPlaningRv.layoutManager = LinearLayoutManager(this)
        binding.treatmentPlaningRv.adapter = medLockerAdapter
        medLockerAdapter.setOnDocumentItemClickListener(object : OnTreatmentPlaningItemClickListener {
            override fun onItemClicked(treatmentPlanningItem: TreatmentPlanningItem) {
                val intent = Intent(this@TreatmentPlanningActivity, AddTreatmentPlanningActivity::class.java)
                intent.putExtra("treatmentPlanning", treatmentPlanningItem)
                startActivity(intent)
            }
        })
    }

    private fun fetchTreatmentPlanning() {
        val userDetails = getUserDetails()
        val request = TreatmentPlaningRequest("", userDetails[User.ID]!!.toInt(), "", "view", userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.treatmentPlaning(req)
        }
        Log.d("requestLoading", request.toString())
    }
}