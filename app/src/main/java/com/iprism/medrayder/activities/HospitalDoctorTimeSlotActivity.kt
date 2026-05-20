package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.SelectDatesAdapter
import com.iprism.medrayder.adapters.SelectTimesAdapter
import com.iprism.medrayder.databinding.ActivityDoctorTimeSlotBinding
import com.iprism.medrayder.interfaces.OnDateItemClickListener
import com.iprism.medrayder.interfaces.OnTimeItemClickListener
import com.iprism.medrayder.models.hospitaldoctortimeslots.HospitalDoctorTimeSlotsRequest
import com.iprism.medrayder.models.onlinedoctorbookingdetails.DatesItem
import com.iprism.medrayder.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.medrayder.models.onlinedoctorbookingdetails.OnlineDoctorBookingDetailsApiResponse
import com.iprism.medrayder.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.medrayder.models.hospitaldoctors.DoctorsItem
import com.iprism.medrayder.repository.HospitalsRepository
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.HospitalDoctorTimeSlotViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class HospitalDoctorTimeSlotActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDoctorTimeSlotBinding
    private var doctor : DoctorsItem? = null
    private lateinit var viewModel: HospitalDoctorTimeSlotViewModel
    private var specialityId : String = ""
    private var date : String = ""
    private var convertDate : String = ""
    private var consultType = ""
    private var fee = ""
    private var hospitalId : String = ""
    private var catId = ""
    private var time : TimesItem? = null
    private var familyMembers : ArrayList<FamilyMembersItem>? = null
    private var onlineDoctorBookingDetailsApiResponse: OnlineDoctorBookingDetailsApiResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorTimeSlotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleContinueBookingBtn()
        if (intent.hasExtra("doctor")) {
            specialityId = intent.getStringExtra("specialityId")!!
            hospitalId = intent.getStringExtra("hospitalId")!!
            doctor = intent.getSerializableExtra("doctor") as DoctorsItem?
            catId = intent.getStringExtra("catId")!!
            consultType = intent.getStringExtra("consultType")!!
            fee = intent.getStringExtra("fee")!!
            showDoctorDetails()
        }
        handleBack()
        initViewModel()
        observeResponse()
        val userDetails = getUserDetails()
        /*if (consultType.equals("online", true)) {
            fee = doctor!!.onlineFee.toString()
        } else {
            fee = doctor!!.offlineFee.toString()
        }*/
        val request = HospitalDoctorTimeSlotsRequest("", doctor!!.id, 0, 0, userDetails[User.ID]!!.toInt(), specialityId.toInt(), fee.toInt(), "dates", userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), hospitalId.toInt(), "")
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getHospitalDoctorBookingDetails(req)
        }
        Log.d("request", request.toString())
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleContinueBookingBtn() {
        binding.continueBookingBtn.setOnClickListener(View.OnClickListener {
            if (date.equals("", true)) {
                showToast(getString(R.string.please_select_date))
            } else if (time == null) {
                showToast(getString(R.string.please_select_time))
            } else {
                val intent = Intent(this, AddHospitalDoctorPatientActivity::class.java)
                intent.putExtra("tag", "doctor")
                intent.putExtra("familyMembers", familyMembers)
                intent.putExtra("doctor", doctor)
                intent.putExtra("date", date)
                intent.putExtra("convertDate", convertDate)
                intent.putExtra("specialityId", specialityId)
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("time", time)
                intent.putExtra("catId", catId)
                intent.putExtra("consultType", consultType)
                intent.putExtra("fee", fee)
                startActivity(intent)
            }
        })
    }

    private fun setUpSelectDates(dates : List<DatesItem>) {
        val selectDatesAdapter = SelectDatesAdapter(dates)
        binding.selectDatesRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.selectDatesRv.adapter = selectDatesAdapter
        selectDatesAdapter.setOnDoctorItemClickListener(object : OnDateItemClickListener{
            override fun onItemClicked(date: DatesItem) {
                this@HospitalDoctorTimeSlotActivity.date = date.date
                this@HospitalDoctorTimeSlotActivity.convertDate = date.convertDate
                this@HospitalDoctorTimeSlotActivity.time = null
                observeResponse1()
                val userDetails = getUserDetails()
                val request = HospitalDoctorTimeSlotsRequest(date.date, doctor!!.id, 0, 0, userDetails[User.ID]!!.toInt(), specialityId.toInt(), fee.toInt(), "slots", userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), hospitalId.toInt(), "")
                NetworkRetryHelper.checkAndCallWithRetry(this@HospitalDoctorTimeSlotActivity, request) { req ->
                    viewModel.getSlots(req)
                }
                Log.d("request", request.toString())
            }
        })
    }

    private fun setUpSelectMorningTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.morningTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.morningTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener{
            override fun onItemClicked(time: TimesItem) {
                this@HospitalDoctorTimeSlotActivity.time = time
                setUpSelectEveningTimes(onlineDoctorBookingDetailsApiResponse!!.response.slots.evening)
                setUpSelectAfternoonTimes(onlineDoctorBookingDetailsApiResponse!!.response.slots.afternoon)
            }
        })
    }

    private fun setUpSelectAfternoonTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.afternoonTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.afternoonTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener{
            override fun onItemClicked(time: TimesItem) {
                this@HospitalDoctorTimeSlotActivity.time = time
                setUpSelectMorningTimes(onlineDoctorBookingDetailsApiResponse!!.response.slots.morning)
                setUpSelectEveningTimes(onlineDoctorBookingDetailsApiResponse!!.response.slots.evening)
            }
        })
    }

    private fun setUpSelectEveningTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.eveningTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.eveningTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener{
            override fun onItemClicked(time: TimesItem) {
                this@HospitalDoctorTimeSlotActivity.time = time
                setUpSelectMorningTimes(onlineDoctorBookingDetailsApiResponse!!.response.slots.morning)
                setUpSelectAfternoonTimes(onlineDoctorBookingDetailsApiResponse!!.response.slots.afternoon)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showDoctorDetails() {
        binding.consultationTxt.text = getString(R.string.book_consultation)
        if (consultType.equals("online", true)) {
            binding.priceTxt.text  = "₹" + doctor!!.onlineFee
        } else {
            binding.priceTxt.text  = "₹" + doctor!!.offlineFee
        }
        binding.nameTxt.text  = doctor!!.name
        binding.specialityTxt.text  = doctor!!.specialization
        binding.hospitalNameTxt.visibility = View.VISIBLE
        binding.hospitalNameTxt.text  = doctor!!.hospitalName
        if (doctor!!.image.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + doctor!!.image)
                .into(binding.doctorImg)
        }
        binding.studyTxt.text  = doctor!!.qualification
        binding.ratingBar.rating = doctor!!.rating.toFloat()
        binding.consultationsCountTxt.text  = "${doctor!!.consultations} ${if (doctor!!.consultations.toInt() > 1) getString(R.string.consultations) else getString(R.string.consultation)}"
        binding.expTxt.text = "${doctor!!.exp} ${if (doctor!!.exp.toInt() > 1) getString(R.string.years) else getString(R.string.year)}"
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalDoctorTimeSlotViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalDoctorTimeSlotViewModel::class.java]
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
                        binding.datesNoTxt.visibility = View.GONE
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

    private fun observeResponse1() {
        viewModel.response1.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.slotsLl.visibility = View.VISIBLE
                    onlineDoctorBookingDetailsApiResponse = result.data
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