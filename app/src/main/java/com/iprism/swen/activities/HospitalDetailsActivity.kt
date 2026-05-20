package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.swen.R
import com.iprism.swen.adapters.SliderImagesAdapter
import com.iprism.swen.databinding.ActivityHospitalDetailsBinding
import com.iprism.swen.databinding.BookAmbulanceBottomSheetBinding
import com.iprism.swen.fragments.HospitalBookingFragment
import com.iprism.swen.fragments.HospitalDiagnosticFragment
import com.iprism.swen.fragments.HospitalMedicineFragment
import com.iprism.swen.models.hospitaldetails.HospitalDetailsRequest
import com.iprism.swen.models.hospitaldetails.ImagesItem
import com.iprism.swen.models.hospitaldetails.MainDataItem
import com.iprism.swen.models.hospitaldetails.SpecialitiesItem
import com.iprism.swen.repository.HospitalsRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.HospitalDetailsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import com.smarteist.autoimageslider.SliderAnimations

class HospitalDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHospitalDetailsBinding
    private var hospitalId = ""
    private var catId = ""
    private var orderType = ""
    private lateinit var viewModel: HospitalDetailsViewModel
    private lateinit var specialitiesItem: List<SpecialitiesItem>
    var lat = ""
    var lon = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("hospitalId")) {
            hospitalId = intent.getStringExtra("hospitalId")!!
            lat = intent.getStringExtra("lat")!!
            lon = intent.getStringExtra("lon")!!
        }
        initViewModel()
        observeDoctorsResponse()
        val userDetails = getUserDetails()
        val request = HospitalDetailsRequest(userDetails[User.ID]!!.toInt(), lon, userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), hospitalId.toInt(), lat)
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getHospitalDetails(req)
        }
        Log.d("request", request.toString())
        handleAdmitBtn()
        handleBack()
        handleContinueBtn()
        handleContinueBookingBtn()
        handleHospitalBookingsLL()
        handleMedicinesLL()
        handleDiagnosticLL()
        handleBookMedicineBtn()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleBookMedicineBtn() {
        binding.bookMedicineImg.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, PrescriptionActivity::class.java)
            intent.putExtra("tag", "hospitalPharmacy")
            intent.putExtra("hospitalId", hospitalId)
            intent.putExtra("orderType", orderType)
            startActivity(intent)
        })
    }

    private fun handleContinueBookingBtn() {
        binding.continueBookingBtn.setOnClickListener(View.OnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as? HospitalBookingFragment
            val specialityId = fragment!!.specialityId
            val specialityName = fragment.specialityName
            if (specialityId.isEmpty()) {
                showToast(getString(R.string.please_select_speciality))
            } else {
                val intent = Intent(this, HospitalDoctorsActivity::class.java)
                intent.putExtra("tag", "hospitalDoctors")
                intent.putExtra("name", specialityName)
                intent.putExtra("id", specialityId)
                intent.putExtra("hospitalId", hospitalId)
                intent.putExtra("catId", catId)
                startActivity(intent)
            }
        })
    }

    private fun handleHospitalBookingsLL() {
        binding.hospitalBookingLl.setOnClickListener(View.OnClickListener {
            switchFragment(HospitalBookingFragment(specialitiesItem))
            binding.hospitalBookingTxt.setTextColor(ContextCompat.getColor(this, R.color.blue))
            binding.medicinesTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.diagnosticTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.hospitalBookingDivider.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            binding.medicinesDivider.setBackgroundColor(Color.parseColor("#A8A8A8"))
            binding.diagnosticDivider.setBackgroundColor(Color.parseColor("#A8A8A8"))
            binding.hospitalBookingDivider.visibility = View.VISIBLE
            binding.medicinesDivider.visibility = View.GONE
            binding.diagnosticDivider.visibility = View.GONE
        })
    }

    private fun handleMedicinesLL() {
        binding.medicinesLl.setOnClickListener(View.OnClickListener {
            switchFragment(HospitalMedicineFragment(hospitalId, orderType))
            binding.medicinesTxt.setTextColor(ContextCompat.getColor(this, R.color.blue))
            binding.hospitalBookingTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.diagnosticTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.medicinesDivider.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            binding.hospitalBookingDivider.setBackgroundColor(Color.parseColor("#A8A8A8"))
            binding.diagnosticDivider.setBackgroundColor(Color.parseColor("#A8A8A8"))
            binding.medicinesDivider.visibility = View.VISIBLE
            binding.hospitalBookingDivider.visibility = View.GONE
            binding.diagnosticDivider.visibility = View.GONE
        })
    }

    private fun handleDiagnosticLL() {
        binding.diagnosticLl.setOnClickListener(View.OnClickListener {
            switchFragment(HospitalDiagnosticFragment(hospitalId))
            binding.diagnosticTxt.setTextColor(ContextCompat.getColor(this, R.color.blue))
            binding.hospitalBookingTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.medicinesTxt.setTextColor(Color.parseColor("#A8A8A8"))
            binding.diagnosticDivider.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            binding.hospitalBookingDivider.setBackgroundColor(Color.parseColor("#A8A8A8"))
            binding.medicinesDivider.setBackgroundColor(Color.parseColor("#A8A8A8"))
            binding.diagnosticDivider.visibility = View.VISIBLE
            binding.medicinesDivider.visibility = View.GONE
            binding.hospitalBookingDivider.visibility = View.GONE
        })
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DiagnosticTimeSlotActivity::class.java))
        })
    }

    private fun switchFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment).commit()
    }

    private fun handleAdmitBtn() {
        binding.admitBtn.setOnClickListener(View.OnClickListener {
            showBookAmbulanceBottomSheet(this)
        })
    }

    private fun setHospitalBanners(images : List<ImagesItem>) {
        val sliderImagesAdapter = SliderImagesAdapter(images)
        binding.imageSlider.setSliderAdapter(sliderImagesAdapter)
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.startAutoCycle()
    }

    fun setView(status : String) {
        if (status.equals("hospitalBooking", true)) {
            binding.bookMedicineImg.visibility = View.GONE
            binding.hospitalBookingLl1.visibility = View.VISIBLE
            binding.diagnosticLl1.visibility = View.GONE
        } else if (status.equals("medicine", true)) {
            binding.bookMedicineImg.visibility = View.VISIBLE
            binding.hospitalBookingLl1.visibility = View.GONE
            binding.diagnosticLl1.visibility = View.GONE
        }  else if (status.equals("diagnostic", true)) {
            //binding.diagnosticLl1.visibility = View.VISIBLE
            binding.hospitalBookingLl1.visibility = View.GONE
            binding.bookMedicineImg.visibility = View.GONE
        }
    }

    private fun showBookAmbulanceBottomSheet(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val bookAmbulanceBottomSheetBinding = BookAmbulanceBottomSheetBinding.inflate(
            LayoutInflater.from(context))
        bottomSheetDialog.setContentView(bookAmbulanceBottomSheetBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        bookAmbulanceBottomSheetBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
            val intent = Intent(this, HospitalTimeSlotActivity::class.java)
            intent.putExtra("hospitalId", hospitalId)
            intent.putExtra("catId", catId)
            startActivity(intent)
        })
        bookAmbulanceBottomSheetBinding.btnContinueBooking.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
            val intent = Intent(this, AmbulanceBookingActivity::class.java)
            intent.putExtra("hospitalId", hospitalId)
            startActivity(intent)
        })
        bottomSheetDialog.show()
    }

    private fun initViewModel() {
        val repository = HospitalsRepository()
        val factory = ViewModelFactory { HospitalDetailsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalDetailsViewModel::class.java]
    }

    private fun observeDoctorsResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    showHospitalDetails(result.data.hospitalDetailsResponse.mainData)
                    binding.nestedScrollView.visibility = View.VISIBLE
                    binding.bottomLl.visibility = View.VISIBLE
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showHospitalDetails(details : MainDataItem) {
        val imageList = details.images.ifEmpty {
            listOf(ImagesItem(url = ""))
        }
        setHospitalBanners(imageList)
        orderType = details.homeDelivery
        catId = details.catId.toString()
        specialitiesItem = details.specialities
        switchFragment(HospitalBookingFragment(details.specialities))
        binding.nameTxt.text = details.name
        binding.descTxt.text = details.description
        binding.timingsTxt.text = details.openTime + " - " + details.closeTime
        binding.locationTxt.text = details.location
    }
}