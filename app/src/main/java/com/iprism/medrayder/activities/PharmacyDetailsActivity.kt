package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.medrayder.adapters.PharmacyCategoriesAdapter
import com.iprism.medrayder.adapters.SliderImagesAdapter
import com.iprism.medrayder.databinding.ActivityPharmacyDetailsBinding
import com.iprism.medrayder.interfaces.OnPharmacyCatItemClickListener
import com.iprism.medrayder.models.hospitaldetails.ImagesItem
import com.iprism.medrayder.models.pharmacyDetails.PharmacyCategory
import com.iprism.medrayder.models.pharmacyDetails.PharmacyDetailsRequest
import com.iprism.medrayder.models.pharmacyDetails.Response
import com.iprism.medrayder.repository.PharmaciesRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.viewmodels.PharmacyDetailsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory
import com.smarteist.autoimageslider.SliderAnimations

class PharmacyDetailsActivity : AppCompatActivity() {
    
    private lateinit var binding : ActivityPharmacyDetailsBinding
    private var pharmacyId = ""
    private var orderTYpe = ""
    private lateinit var viewModel: PharmacyDetailsViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmacyDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("pharmacyId")) {
            pharmacyId = intent.getStringExtra("pharmacyId")!!
            orderTYpe = intent.getStringExtra("orderType")!!
        }
        handleBack()
        handleBookMedicineBtn()
        handlePrescriptionLL()
        initViewModel()
        observeResponse()
        val userDetails = getUserDetails()
        val request = PharmacyDetailsRequest(userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), pharmacyId.toInt())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchPharmacyDetails(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleBookMedicineBtn() {
        binding.bookMedicineImg.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, PrescriptionActivity::class.java)
            intent.putExtra("tag", "pharmacy")
            intent.putExtra("pharmacyId", pharmacyId)
            intent.putExtra("orderType", orderTYpe)
            startActivity(intent)
        })
    }

    private fun handlePrescriptionLL() {
        binding.prescriptionLl.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, PrescriptionActivity::class.java)
            intent.putExtra("tag", "pharmacy")
            intent.putExtra("pharmacyId", pharmacyId)
            intent.putExtra("orderType", orderTYpe)
            startActivity(intent)
        })
    }

    private fun setupPharmacyCategoriesAdapter(pharmacyCategories : List<PharmacyCategory>) {
        val pharmacyCategoriesAdapter = PharmacyCategoriesAdapter(pharmacyCategories)
        binding.categoriesRv.layoutManager = GridLayoutManager(this, 4)
        binding.categoriesRv.adapter = pharmacyCategoriesAdapter
        pharmacyCategoriesAdapter.setOnDoctorItemClickListener(object : OnPharmacyCatItemClickListener  {
            override fun onItemClicked(catId: String, catName : String) {
                val intent = Intent(this@PharmacyDetailsActivity, PharmacyProductsActivity::class.java)
                intent.putExtra("pharmacyId", pharmacyId)
                intent.putExtra("catId", catId)
                intent.putExtra("catName", catName)
                intent.putExtra("orderType", orderTYpe)
                startActivity(intent)
            }
        })
    }

    private fun setPharmacyBanners(images : List<ImagesItem>) {
        val sliderImagesAdapter = SliderImagesAdapter(images)
        binding.imageSlider.setSliderAdapter(sliderImagesAdapter)
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider.startAutoCycle()
    }

    private fun initViewModel() {
        val repository = PharmaciesRepository()
        val factory = ViewModelFactory { PharmacyDetailsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[PharmacyDetailsViewModel::class.java]
    }

    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.scrollView.visibility = View.VISIBLE
                    binding.bookMedicineImg.visibility = View.VISIBLE
                    Log.d("response1", result.data.response.toString())
                    showPharmacyDetails(result.data.response)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showPharmacyDetails(details : Response) {
        setPharmacyBanners(details.mainData.images)
        setupPharmacyCategoriesAdapter(details.mainData.pharmacyCategories)
        binding.pharmacyNameTxt.text = details.mainData.name
        binding.descTxt.text = details.mainData.description
        binding.timingsTxt.text = details.mainData.openTime + " - " + details.mainData.closeTime
        binding.locationTxt.text = details.mainData.location
    }
}