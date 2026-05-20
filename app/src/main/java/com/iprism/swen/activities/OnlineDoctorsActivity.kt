package com.iprism.swen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.swen.R
import com.iprism.swen.adapters.OnlineDoctorSpecialitiesAdapter
import com.iprism.swen.databinding.ActivityOnlineDoctorsBinding
import com.iprism.swen.interfaces.OnSpecialityItemClickListener
import com.iprism.swen.models.onlinedoctorspeacilities.OnlineDoctorSpecialitiesRequest
import com.iprism.swen.models.onlinedoctorspeacilities.SpecialitiesItem
import com.iprism.swen.repository.OnlineDoctorRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.OnlineDoctorSpecialitiesViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class OnlineDoctorsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOnlineDoctorsBinding
    private lateinit var viewModel: OnlineDoctorSpecialitiesViewModel
    private var specialityId = ""
    private var specialityName = ""

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        binding = ActivityOnlineDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleContinueBtn()
        handleBack()
        initViewModel()
        observeLoginResponse()
        val userDetails = getUserDetails()
        val request = OnlineDoctorSpecialitiesRequest(userDetails[User.ID].toString(), userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getOnlineDoctorSpecialities(req)
        }
        Log.d("request", request.toString())
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            if (specialityId.isEmpty()) {
                showToast(getString(R.string.please_select_speciality))
            } else {
                val intent = Intent(this, DoctorsActivity::class.java)
                intent.putExtra("tag", "onlineDoctors")
                intent.putExtra("name", specialityName)
                intent.putExtra("id", specialityId)
                startActivity(intent)
            }
        })
    }

    private fun setUpSpecialities(specialitiesItems: List<SpecialitiesItem>) {
        val onlineDoctorSpecialitiesAdapter = OnlineDoctorSpecialitiesAdapter(specialitiesItems)
        binding.specilitiesRv.layoutManager = GridLayoutManager(this, 3)
        binding.specilitiesRv.adapter = onlineDoctorSpecialitiesAdapter
        onlineDoctorSpecialitiesAdapter.setOnArtistActionListener(object : OnSpecialityItemClickListener{
            override fun onItemClicked(id: String, name : String) {
                specialityId = id
                specialityName = name
            }
        })
    }

    private fun initViewModel() {
        val repository = OnlineDoctorRepository()
        val factory = ViewModelFactory { OnlineDoctorSpecialitiesViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[OnlineDoctorSpecialitiesViewModel::class.java]
    }

    private fun observeLoginResponse() {
        viewModel.specialities.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    setUpSpecialities(result.data)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }
}