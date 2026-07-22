package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.adapters.SelectDatesAdapter
import com.iprism.swen.adapters.SelectTimesAdapter
import com.iprism.swen.databinding.ActivityAppointmentDoctorsTimeSlotBinding
import com.iprism.swen.interfaces.OnDateItemClickListener
import com.iprism.swen.interfaces.OnTimeItemClickListener
import com.iprism.swen.models.onlinedoctorbookingdetails.DatesItem
import com.iprism.swen.models.onlinedoctorbookingdetails.FamilyMembersItem
import com.iprism.swen.models.onlinedoctorbookingdetails.TimesItem
import com.iprism.swen.models.surgeondoctorbookingdetails.SurgeonDoctorBookingDetailsApiResponse
import com.iprism.swen.models.surgeondoctorbookingdetails.SurgeonDoctorBookingDetailsRequest
import com.iprism.swen.models.surgeonsymptomdoctors.DoctorsItem
import com.iprism.swen.repository.SurgeonSymptomsDoctorRepository
import com.iprism.swen.utils.Constants
import com.iprism.swen.utils.DRY
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.SurgeonDoctorTimeSlotViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import kotlin.text.toInt
import kotlin.toString

class SurgeonDoctorTimeSlotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentDoctorsTimeSlotBinding
    private var doctor : DoctorsItem? = null
    private lateinit var viewModel: SurgeonDoctorTimeSlotViewModel
    private var symptomId = ""
    private var hospitalId = ""
    private var date : String = ""
    private var convertDate : String = ""
    private var lat = ""
    private var lon = ""
    private var time : TimesItem? = null
    private var familyMembers : ArrayList<FamilyMembersItem>? = null
    private var apiResponse: SurgeonDoctorBookingDetailsApiResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAppointmentDoctorsTimeSlotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (intent.hasExtra("doctor")) {
            symptomId = intent.getStringExtra("symptomId")!!
            hospitalId = intent.getStringExtra("hospitalId")!!
            doctor = intent.getSerializableExtra("doctor") as DoctorsItem?
            showDoctorDetails()
        }
        handleLocationLl()
        handleBack()
        handleContinueBtn()
        initViewModel()
        observeResponse()
        val userDetails = getUserDetails()
        val request = SurgeonDoctorBookingDetailsRequest(
            symptomId.toInt(),
            "",
            doctor!!.id,
            0,
            0,
            userDetails[User.ID]!!.toInt(),
            doctor!!.fee,
            "dates",
            userDetails[User.AUTH_TOKEN].toString(),
            hospitalId.toInt(),
            0
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchSurgeonDoctorBookingDetails(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleLocationLl() {
        binding.locationLl.setOnClickListener(View.OnClickListener {
            DRY.showMaps(this, lat, lon)
        })
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener { view ->
            if (date.equals("", true)) {
                showToast("Please Select Date")
            } else if (time == null) {
                showToast("Please Select Time")
            } else {
                val intent = Intent(this, AddPatientActivity::class.java)
                intent.putExtra("tag", "surgeonDoctor")
                intent.putExtra("familyMembers", familyMembers)
                intent.putExtra("doctor", doctor)
                intent.putExtra("date", date)
                intent.putExtra("convertDate", convertDate)
                intent.putExtra("specialityId", symptomId)
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("time", time)
                startActivity(intent)
            }
        }
    }

    private fun setUpSelectDates(dates : List<DatesItem>) {
        val selectDatesAdapter = SelectDatesAdapter(dates)
        binding.selectDatesRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.selectDatesRv.adapter = selectDatesAdapter
        selectDatesAdapter.setOnDoctorItemClickListener(object : OnDateItemClickListener {
            override fun onItemClicked(date: DatesItem) {
                this@SurgeonDoctorTimeSlotActivity.date = date.date
                this@SurgeonDoctorTimeSlotActivity.convertDate = date.convertDate
                this@SurgeonDoctorTimeSlotActivity.time = null
                observeResponse1()
                val userDetails = getUserDetails()
                val request = SurgeonDoctorBookingDetailsRequest(symptomId.toInt(), date.date, doctor!!.id, 0, 0, userDetails[User.ID]!!.toInt(),doctor!!.fee, "slots", userDetails[User.AUTH_TOKEN].toString(), hospitalId.toInt(), 0)
                NetworkRetryHelper.checkAndCallWithRetry(this@SurgeonDoctorTimeSlotActivity, request) { req ->
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
                this@SurgeonDoctorTimeSlotActivity.time = time
                setUpSelectEveningTimes(apiResponse!!.response.slots.evening)
                setUpSelectAfternoonTimes(apiResponse!!.response.slots.afternoon)
            }
        })
    }

    private fun setUpSelectAfternoonTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.afternoonTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.afternoonTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener{
            override fun onItemClicked(time: TimesItem) {
                this@SurgeonDoctorTimeSlotActivity.time = time
                setUpSelectMorningTimes(apiResponse!!.response.slots.morning)
                setUpSelectEveningTimes(apiResponse!!.response.slots.evening)
            }
        })
    }

    private fun setUpSelectEveningTimes(times : List<TimesItem>) {
        val selectTimesAdapter = SelectTimesAdapter(times)
        binding.eveningTimesRv.layoutManager = GridLayoutManager(this, 3)
        binding.eveningTimesRv.adapter = selectTimesAdapter
        selectTimesAdapter.setOnDoctorItemClickListener(object : OnTimeItemClickListener {
            override fun onItemClicked(time: TimesItem) {
                this@SurgeonDoctorTimeSlotActivity.time = time
                setUpSelectMorningTimes(apiResponse!!.response.slots.morning)
                setUpSelectAfternoonTimes(apiResponse!!.response.slots.afternoon)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showDoctorDetails() {
        binding.nameTxt.text  = doctor!!.name
        binding.priceTxt.text  = "₹" + doctor!!.fee
        binding.specialityTxt.text  = doctor!!.specialization
        binding.addressTxt.text  = doctor!!.location
        lat = doctor!!.lat
        lon = doctor!!.lon
        if (doctor!!.image.isNotEmpty()) {
            Glide.with(this)
                .load(Constants.IMAGES_BASE_URL + doctor!!.image)
                .into(binding.doctorImg)
        }
        binding.studyTxt.text  = doctor!!.qualification
        binding.ratingBar.rating = doctor!!.rating.toFloat()
        binding.consultationsCountTxt.text  = "${doctor!!.consultations} ${if (doctor!!.consultations.toInt() > 1) getString(
            R.string.consultations) else getString(R.string.consultation)}"
        binding.expTxt.text = "${doctor!!.exp} ${if (doctor!!.exp.toInt() > 1) getString(R.string.years) else getString(R.string.year)}"
    }

    private fun initViewModel() {
        val repository = SurgeonSymptomsDoctorRepository()
        val factory = ViewModelFactory { SurgeonDoctorTimeSlotViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[SurgeonDoctorTimeSlotViewModel::class.java]
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
                    apiResponse = result.data
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