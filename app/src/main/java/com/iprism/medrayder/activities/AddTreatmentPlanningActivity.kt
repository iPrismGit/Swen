package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.TreatmentPlaningAdapter
import com.iprism.medrayder.databinding.ActivityAddTreatmentPlanningBinding
import com.iprism.medrayder.interfaces.OnTreatmentPlaningItemClickListener
import com.iprism.medrayder.models.treatmentplaning.TreatmentPlaningRequest
import com.iprism.medrayder.models.treatmentplaning.TreatmentPlanningItem
import com.iprism.medrayder.repository.CommonRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.setEnabledState
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.TreatmentPlanningViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory
import java.util.Calendar

class AddTreatmentPlanningActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddTreatmentPlanningBinding
    private lateinit var viewModel: TreatmentPlanningViewModel
    private var treatmentPlanningItem: TreatmentPlanningItem? = null

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTreatmentPlanningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("treatmentPlanning")) {
            treatmentPlanningItem = intent.getSerializableExtra("treatmentPlanning") as TreatmentPlanningItem
            binding.confirmBtn.visibility = View.GONE
            binding.nameEt.setText(treatmentPlanningItem!!.name)
            binding.nameEt.isFocusable = false
            binding.nameEt.isEnabled = false
            binding.nameEt.isActivated = false
            binding.addDateTxt.text = treatmentPlanningItem!!.date
        } else {
            initViewModel()
            observeResponse()
            handleConfirm()
            handleDateLL()
        }
        handleBack()
    }


    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleDateLL() {
        binding.addDateTxt.setOnClickListener(View.OnClickListener {
            getDate(binding.addDateTxt)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun getDate(dateTxt: TextView) {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(dateTxt.context, { view, year, monthOfYear, dayOfMonth ->
            dateTxt.text = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
        }, year, month, day)
        datePickerDialog.datePicker.minDate = c.timeInMillis
        datePickerDialog.show()
    }

    private fun getName() : String {
        return binding.nameEt.text.toString().trim()
    }

    private fun getDate() : String {
        return binding.addDateTxt.text.toString().trim()
    }

    private fun handleConfirm() {
        binding.confirmBtn.setOnClickListener(View.OnClickListener {
            if (getName().isEmpty()) {
                showToast(getString(R.string.please_enter_name))
            } else if (getDate().isEmpty() || getDate().equals("Add Date", true)) {
                showToast(getString(R.string.please_select_date))
            } else {
                addTreatmentPlanning()
            }
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
                    binding.confirmBtn.setEnabledState(false)
                }

                is UiState.Success -> {
                    binding.confirmBtn.setEnabledState(true)
                    binding.progress.hideProgress()
                    finish()
                }

                is UiState.Error -> {
                    binding.confirmBtn.setEnabledState(true)
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun addTreatmentPlanning() {
        val userDetails = getUserDetails()
        val request = TreatmentPlaningRequest(getDate(), userDetails[User.ID]!!.toInt(), getName(), "insert", userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.treatmentPlaning(req)
        }
        Log.d("requestLoading", request.toString())
    }
}