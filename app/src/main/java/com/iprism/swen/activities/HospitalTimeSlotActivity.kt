package com.iprism.swen.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.adapters.SelectDatesAdapter
import com.iprism.swen.adapters.SelectTimesAdapter
import com.iprism.swen.databinding.ActivityHospitalTimeSlotBinding
import com.iprism.swen.interfaces.OnDateItemClickListener
import com.iprism.swen.interfaces.OnTimeItemClickListener
import com.iprism.swen.models.admit.AdmitBookingApiResponse
import com.iprism.swen.models.admit.AdmitBookingRequest
import com.iprism.swen.models.onlinedoctorbookingdetails.DatesItem
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.HospitalAdmitViewModel
import com.iprism.swen.viewmodels.ViewModelFactory


class HospitalTimeSlotActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHospitalTimeSlotBinding
    private lateinit var viewModel: HospitalAdmitViewModel
    private var date : String = ""
    private var convertDate : String = ""
    private var hospitalId : String = ""
    private var catId : String = ""
    private var lat : String = ""
    private var lon : String = ""
    private var time : TimesItem? = null
    private var familyMembers : ArrayList<FamilyMembersItem>? = null
    private var slotsApiResponse: AdmitBookingApiResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalTimeSlotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("hospitalId")) {
            hospitalId = intent.getStringExtra("hospitalId")!!
            catId = intent.getStringExtra("catId")!!
        }
        handleBack()
        initViewModel()
        observeResponse()
        observeSlots()
        handleContinueBookingBtn()
        handleLocationLl()
        val userDetails = getUserDetails()
        val request = AdmitBookingRequest("", userDetails[User.ID]!!.toInt(), 0, 0, "", "", "dates", userDetails[User.LANG].toString(), "", userDetails[User.AUTH_TOKEN].toString(), hospitalId.toInt(), 0)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getDates(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleContinueBookingBtn() {
        binding.continueBookingBtn.setOnClickListener(View.OnClickListener {
            if (date.isEmpty()) {
                showToast(getString(R.string.please_select_date))
            } else if (time == null) {
                showToast(getString(R.string.please_select_time))
            } else {
                val intent = Intent(this, AddAdmitPatientActivity::class.java)
                intent.putExtra("tag", "hospitalAdmit")
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("catId", catId)
                intent.putExtra("date", date)
                intent.putExtra("convertDate", convertDate)
                intent.putExtra("time", time)
                intent.putExtra("familyMembers", familyMembers)
                startActivity(intent)
            }
        })
    }

    private fun handleLocationLl() {
        binding.locationLl.setOnClickListener(View.OnClickListener {
            val url = "https://www.google.com/maps/search/?api=1&query=$lat,$lon"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        })
    }

    private fun setUpSelectDates(dates : List<DatesItem>) {
        val selectDatesAdapter = SelectDatesAdapter(dates)
        binding.selectDatesRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.selectDatesRv.adapter = selectDatesAdapter
        selectDatesAdapter.setOnDoctorItemClickListener(object : OnDateItemClickListener {
            override fun onItemClicked(date: DatesItem) {
                this@HospitalTimeSlotActivity.date = date.date
                this@HospitalTimeSlotActivity.date = date.date
                this@HospitalTimeSlotActivity.time = null
                val userDetails = getUserDetails()
                val request = AdmitBookingRequest(date.date, userDetails[User.ID]!!.toInt(), 0, 0, "", "", "slots", userDetails[User.LANG].toString(), "", userDetails[User.AUTH_TOKEN].toString(), hospitalId.toInt(), 0)
                NetworkRetryHelper.checkAndCallWithRetry(this@HospitalTimeSlotActivity, request) { req ->
                    viewModel.getSlots(req)
                }
                Log.d("requestLoading", request.toString())
            }
        })
    }

    private fun setUpSelectMorningTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.morningTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.morningTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener {
            override fun onItemClicked(time: TimesItem) {
                this@HospitalTimeSlotActivity.time = time
                setUpSelectEveningTimes(slotsApiResponse!!.response.slots.evening)
                setUpSelectAfternoonTimes(slotsApiResponse!!.response.slots.afternoon)
            }
        })
    }

    private fun setUpSelectAfternoonTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.afternoonTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.afternoonTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener {
            override fun onItemClicked(time: TimesItem) {
                this@HospitalTimeSlotActivity.time = time
                setUpSelectMorningTimes(slotsApiResponse!!.response.slots.morning)
                setUpSelectEveningTimes(slotsApiResponse!!.response.slots.evening)
            }
        })
    }

    private fun setUpSelectEveningTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.eveningTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.eveningTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener {
            override fun onItemClicked(time: TimesItem) {
                this@HospitalTimeSlotActivity.time = time
                setUpSelectMorningTimes(slotsApiResponse!!.response.slots.morning)
                setUpSelectAfternoonTimes(slotsApiResponse!!.response.slots.afternoon)
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalAdmitViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalAdmitViewModel::class.java]
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
                    if (result.data.response.dates.isNotEmpty()) {
                        setUpSelectDates(result.data.response.dates)
                        binding.nameTxt.text = result.data.response.mainData.name
                        binding.specialityTxt.text = result.data.response.mainData.tagline
                        binding.locationTxt.text = result.data.response.mainData.location
                        binding.locationTxt1.text = result.data.response.mainData.location
                        lat = result.data.response.mainData.lat
                        lon = result.data.response.mainData.lon
                        if (result.data.response.mainData.logo.isNotEmpty()) {
                            Glide.with(this)
                                .load(Constants.IMAGES_BASE_URL + result.data.response.mainData.logo)
                                .into(binding.profileImg)
                        }
                        binding.datesNoTxt.visibility = View.GONE
                        binding.scrollView.visibility = View.VISIBLE
                        binding.divider3.visibility = View.VISIBLE
                        binding.continueBookingBtn.visibility = View.VISIBLE
                    } else {
                        binding.datesNoTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeSlots() {
        viewModel.slots.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.slotsLl.visibility = View.VISIBLE
                    slotsApiResponse = result.data
                    familyMembers = result.data.response.familyMembers
                    binding.morningTimesRv.adapter = null
                    binding.afternoonTimesRv.adapter = null
                    binding.eveningTimesRv.adapter = null
                    if (result.data.response.slots.morning.isNotEmpty()){
                        binding.morningNoSlotsTxt.visibility = View.GONE
                        setUpSelectMorningTimes(result.data.response.slots.morning)
                    } else {
                        binding.morningNoSlotsTxt.visibility = View.VISIBLE
                    }
                    if (result.data.response.slots.afternoon.isNotEmpty()){
                        binding.afternoonNoSlotsTxt.visibility = View.GONE
                        setUpSelectAfternoonTimes(result.data.response.slots.afternoon)
                    } else {
                        binding.afternoonNoSlotsTxt.visibility = View.VISIBLE
                    }
                    if (result.data.response.slots.evening.isNotEmpty()){
                        binding.eveningNoSlotsTxt.visibility = View.GONE
                        setUpSelectEveningTimes(result.data.response.slots.evening)
                    } else {
                        binding.eveningNoSlotsTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }
}