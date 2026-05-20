package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.MedicineBookedAdapter
import com.iprism.medrayder.databinding.ActivityHospitalMedicineBookingDetailsBinding
import com.iprism.medrayder.models.hospitalmedbookingdetails.BookingDetails
import com.iprism.medrayder.models.hospitalmedbookingdetails.HospitalMedicineBookingDetailsRequest
import com.iprism.medrayder.models.hospitalmedbookingdetails.ProductsItem
import com.iprism.medrayder.repository.HospitalsMedicineRepository
import com.iprism.medrayder.utils.DRY
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.HospitalMedBookingsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class HospitalMedicineBookingDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHospitalMedicineBookingDetailsBinding
    private lateinit var viewModel: HospitalMedBookingsViewModel
    private var id = ""
    private var bookingType = ""
    private var image = ""
    private var lat = ""
    private var lon = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalMedicineBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id")!!
            bookingType = intent.getStringExtra("bookingType")!!
        }
        handleBack()
        handleNeedHelp()
        handlePrescriptionLL()
        handlePrescriptionLL()
        handlePickupLocation()
        handleHomeDeliveryLocation()
        initViewModel()
        observeResponse()
        fetchBookingDetails()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleNeedHelp() {
        binding.needHelpTxt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
        })
    }

    private fun handlePickupLocation() {
        binding.pickUpLocationLl.setOnClickListener(View.OnClickListener {
            DRY.showMaps(this, lat, lon)
        })
    }

    private fun handleHomeDeliveryLocation() {
        binding.deliveryAddressLl.setOnClickListener(View.OnClickListener {
            DRY.showMaps(this, lat, lon)
        })
    }

    private fun handlePrescriptionLL() {
        binding.prescriptionLl.setOnClickListener(View.OnClickListener {
            if (!image.endsWith(".pdf")) {
                val intent = Intent(this, ViewDocumentsActivity::class.java)
                val imageList = ArrayList<String>()
                imageList.add(image)
                intent.putStringArrayListExtra("images", imageList)
                intent.putExtra("name", getString(R.string.prescription))
                startActivity(intent)
            } else {
                val intent = Intent(this, PdfViewActivity::class.java)
                intent.putExtra("pdfUrl", image)
                intent.putExtra("name", getString(R.string.prescription)    )
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalsMedicineRepository()
        val factory = ViewModelFactory { HospitalMedBookingsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalMedBookingsViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.bookingDetails.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    showDetails(result.data.response.bookingDetails)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    showToast(result.message)
                }
            }
        }
    }

    private fun fetchBookingDetails() {
        val userDetails = getUserDetails()
        val request = HospitalMedicineBookingDetailsRequest(id, userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), bookingType, userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchHospitalMedBookingDetails(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun setupProducts(products : List<ProductsItem>) {
        val medicineBookedAdapter = MedicineBookedAdapter(products)
        binding.productsRv.layoutManager = LinearLayoutManager(this)
        binding.productsRv.adapter = medicineBookedAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun showDetails(details : BookingDetails) {
        binding.scrollView.visibility = View.VISIBLE
        binding.recipientNameTxt.setText(details.name)
        binding.recipientMobileTxt.setText(details.mobile)
        binding.itemTotalTxt.text = "₹${details.fee}"
        binding.totalDiscountTxt.text = "₹${details.couponDiscount}"
        binding.totalAmountTxt.text = "₹${details.consultationFee}"
        binding.paymentModeTxt.text = details.paymentType
        binding.bookingIdTxt.text = details.bookingId
        if (details.orderType.equals("home_delivery", true)) {
            binding.arrivingTxt.text = getString(R.string.home_delivery)
            binding.deliveryAddressLl.visibility = View.VISIBLE
            binding.pickUpLocationLl.visibility = View.GONE
            binding.addressTypeTxt.text = details.address.addressType
            binding.addressTxt1.text = listOf(
                details.address.hno,
                details.address.buildingNo,
                details.address.landmark,
                details.address.address
            ).filter { !it.isNullOrBlank() }
                .joinToString(", ")
            lat = details.address.lat
            lon = details.address.lon
        } else {
            binding.arrivingTxt.text = getString(R.string.in_store_pickup)
            binding.deliveryAddressLl.visibility = View.GONE
            binding.pickUpLocationLl.visibility = View.VISIBLE
            binding.pickUpLocationTxt.text = details.address.colony
            lat = details.address.lat
            lon = details.address.lon
        }
        if (details.bookingStatus.equals("completed", true)) {
            DRY.updateOrderStatus(
                this,
                "3",
                binding.stepPlaced,
                binding.stepProcessed,
                binding.stepCompleted,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.labelCompleted,
                binding.progressLine,
                binding.progressLine2
            )
        } else if (details.bookingStatus.equals("pickedup", true)) {
            DRY.updateOrderStatus(
                this,
                "2",
                binding.stepPlaced,
                binding.stepProcessed,
                binding.stepCompleted,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.labelCompleted,
                binding.progressLine,
                binding.progressLine2
            )
        } else {
            DRY.updateOrderStatus(
                this,
                "1", // or dynamic from API
                binding.stepPlaced,
                binding.stepProcessed,
                binding.stepCompleted,
                binding.labelPlaced,
                binding.labelProcessed,
                binding.labelCompleted,
                binding.progressLine,
                binding.progressLine2
            )
        }
        if (!details.bookingType.equals("product_booking", true)) {
            if (details.image.isEmpty()) {
                binding.prescriptionLl.visibility = View.GONE
            } else {
                binding.prescriptionLl.visibility = View.VISIBLE
                binding.billS.visibility = View.GONE
                binding.prescriptionImgTxt.text = details.image
                image = details.image
            }
        }
        setupProducts(details.products)
    }
}