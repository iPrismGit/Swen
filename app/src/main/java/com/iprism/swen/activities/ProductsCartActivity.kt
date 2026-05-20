package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.swen.R
import com.iprism.swen.adapters.PharmacyCartProductsAdapter
import com.iprism.swen.databinding.ActivityProductsCartBinding
import com.iprism.swen.databinding.OnlineOrderTypeBsBinding
import com.iprism.swen.interfaces.OnCartProductItemClickListener
import com.iprism.swen.models.bookpharmacyproduct.BookPharmacyProductRequest
import com.iprism.swen.models.onlinedoctorscoupons.CouponsItem
import com.iprism.swen.models.pharmacyaddtocart.PharmacyProductAddToCartRequest
import com.iprism.swen.models.pharmacyproductcart.PharmacyProductCartApiResponse
import com.iprism.swen.models.pharmacyproductcart.PharmacyProductCartRequest
import com.iprism.swen.models.pharmacyproductcart.ProductsItem
import com.iprism.swen.repository.PharmaciesRepository
import com.iprism.swen.utils.DRY
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.PharmacyProductCartViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class ProductsCartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductsCartBinding
    private lateinit var viewModel: PharmacyProductCartViewModel
    private lateinit var cartResponse: PharmacyProductCartApiResponse
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var coupon: CouponsItem? = null
    private var pharmacyId = ""
    private var catId = ""
    private var orderType = ""
    private var subscriptionStatus = ""
    private var lat = ""
    private var lon = ""
    private lateinit var request : PharmacyProductCartRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("pharmacyId")) {
            pharmacyId = intent.getStringExtra("pharmacyId")!!
            catId = intent.getStringExtra("catId")!!
           // orderType = intent.getStringExtra("orderType")!!
        }
        handleBack()
        handlePayNowBtn()
        handleChangeTxt()
        handleSelectBtn()
        handlePickupLocation()
        handleHomeDeliveryRb()
        handlePickupRb()
        initViewModel()
        observeResponse()
        observeAddToCartResponse()
        observeBookingResponse()
        getCart()
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data!!.hasExtra("tag")) {
                    getCart()
                } else if (data.hasExtra("coupon")){
                    coupon = data.getSerializableExtra("coupon") as CouponsItem
                    binding.couponTxt.text = coupon!!.name
                    getCart()
                } else {
                    getCart()
                }
            }
        }
    }

    private fun handleHomeDeliveryRb() {
        binding.homeDeliveryRb.setOnClickListener(View.OnClickListener {
            binding.deliveryAddressLl.visibility = View.VISIBLE
            binding.pickUpLocationLl.visibility = View.GONE
            orderType = "home_delivery"
        })
    }

    private fun handlePickupRb() {
        binding.pickupOrderRb.setOnClickListener(View.OnClickListener {
            binding.deliveryAddressLl.visibility = View.GONE
            binding.pickUpLocationLl.visibility = View.VISIBLE
            orderType = "pickup"
        })
    }

    private fun getCart() {
        val userDetails = getUserDetails()
        if (coupon != null) {
            request = PharmacyProductCartRequest(coupon!!.percentage, coupon!!.id, userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), orderType)
        } else {
            request = PharmacyProductCartRequest(0, 0, userDetails[User.ID]!!.toInt(), userDetails[User.LANG].toString(), userDetails[User.AUTH_TOKEN].toString(), orderType)
        }
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.getCart(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handlePickupLocation() {
        binding.pickUpLocationLl.setOnClickListener(View.OnClickListener {
            DRY.showMaps(this, lat, lon)
        })
    }

    private fun handlePayNowBtn() {
        binding.payNowBtn.setOnClickListener(View.OnClickListener {
            if (subscriptionStatus.equals("no", true)) {
                val intent = Intent(this, SubscriptionActivity::class.java)
                intent.putExtra("tag", "booking")
                launcher.launch(intent)
            } else {
                showOrderTypeBs(this)
            }
        })
    }

    private fun setUpCartProducts(products : List<ProductsItem>) {
        val pharmacyCartProductsAdapter = PharmacyCartProductsAdapter(products)
        binding.productsCartRv.layoutManager = LinearLayoutManager(this)
        binding.productsCartRv.adapter = pharmacyCartProductsAdapter
        pharmacyCartProductsAdapter.setOnCartProductItemClickListener(object : OnCartProductItemClickListener {

            override fun onIncrementClicked(product: ProductsItem) {
                addToCartProduct(product, "increment")
            }

            override fun onDecrementClicked(product: ProductsItem) {
                addToCartProduct(product, "decrement")
            }
        })
    }

    private fun handleChangeTxt() {
        binding.changeTxt.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            launcher.launch(intent)
        })
    }

    private fun handleSelectBtn() {
        binding.selectBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, OffersActivity::class.java)
            intent.putExtra("tag", "pharmacy")
            launcher.launch(intent)
        })
    }

    private fun initViewModel() {
        val repository = PharmaciesRepository()
        val factory = ViewModelFactory { PharmacyProductCartViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[PharmacyProductCartViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            Log.d("result1", result.toString())
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    cartResponse = result.data
                    showDetails(result)
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.scrollView.visibility = View.GONE
                        binding.divider3.visibility = View.GONE
                        binding.payNowLl.visibility = View.GONE
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeAddToCartResponse() {
        viewModel.addToCartResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    getCart()
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeBookingResponse() {
        viewModel.bookingResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    startActivity(Intent(this, SuccessActivity::class.java))
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun addToCartProduct(product : ProductsItem, viewType : String) {
        val userDetails = getUserDetails()
        val request = PharmacyProductAddToCartRequest(
            0,
            userDetails[User.ID]!!.toInt(),
            product.pharmacyId,
            product.price,
            product.productId,
            product.catId,
            viewType,
            userDetails[User.AUTH_TOKEN].toString(),
            product.originalPrice.toString()
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.addToCartPharmacyProduct(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun bookPharmacyProduct(paymentType : String) {
        val userDetails = getUserDetails()
        val request : BookPharmacyProductRequest
        if (coupon != null) {
            request = BookPharmacyProductRequest("12345", cartResponse.response.billSummary.consultationFee, cartResponse.response.billSummary.couponPercentage, cartResponse.response.billSummary.fee, cartResponse.response.address.id, cartResponse.response.receiptDetails.mobile, coupon!!.name, paymentType, cartResponse.response.billSummary.couponId, userDetails[User.ID]!!.toInt(), cartResponse.response.products[0].pharmacyId, cartResponse.response.products[0].catId, cartResponse.response.receiptDetails.name, userDetails[User.AUTH_TOKEN].toString(), orderType, cartResponse.response.billSummary.couponDiscount, cartResponse.response.walletAmount)
        } else {
            request = BookPharmacyProductRequest("12345", cartResponse.response.billSummary.consultationFee, "0", cartResponse.response.billSummary.fee, cartResponse.response.address.id, cartResponse.response.receiptDetails.mobile, "", paymentType, 0, userDetails[User.ID]!!.toInt(), cartResponse.response.products[0].pharmacyId, cartResponse.response.products[0].catId, cartResponse.response.receiptDetails.name, userDetails[User.AUTH_TOKEN].toString(), orderType, "0", cartResponse.response.walletAmount)
        }
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.bookPharmacyProduct(req)
        }
        Log.d("requestLoading", request.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun showDetails(result: UiState.Success<PharmacyProductCartApiResponse>) {
        subscriptionStatus = result.data.response.subscriptionStatus
        binding.scrollView.visibility = View.VISIBLE
        binding.divider3.visibility = View.VISIBLE
        binding.payNowLl.visibility = View.VISIBLE
        binding.cartItemsTxt.text = "${result.data.response.products.size} ${getString(R.string.items_in_your_cart)}"
        binding.recipientNameTxt.setText(result.data.response.receiptDetails.name)
        binding.recipientMobileTxt.setText(result.data.response.receiptDetails.mobile)
        binding.itemTotalTxt.text = "₹${result.data.response.billSummary.fee}"
        binding.totalDiscountTxt.text = "₹${result.data.response.billSummary.couponDiscount}"
        binding.totalAmountTxt.text = "₹${result.data.response.billSummary.consultationFee}"
        binding.priceTxt.text = "₹${result.data.response.billSummary.consultationFee}"
        if (result.data.response.orderType.equals("yes", true)) {
            orderType = "home_delivery"
            binding.orderTypeRg.visibility = View.VISIBLE
        } else {
            orderType = "pickup"
            binding.orderTypeRg.visibility = View.GONE
            binding.orderTypeTxt.visibility = View.GONE
        }
        if (orderType.equals("home_delivery", true)) {
            binding.homeDeliveryRb.isChecked = true
            binding.deliveryAddressLl.visibility = View.VISIBLE
            binding.pickUpLocationLl.visibility = View.GONE
        } else {
            binding.deliveryAddressLl.visibility = View.GONE
            binding.pickUpLocationLl.visibility = View.VISIBLE
        }
        binding.addressTypeTxt.text = result.data.response.address.addressType
        binding.addressTxt.text = listOf(
            result.data.response.address.hno,
            result.data.response.address.buildingNo,
            result.data.response.address.landmark,
            result.data.response.address.address
        ).filter { !it.isNullOrBlank() }
            .joinToString(", ")
        binding.pickUpLocationTxt.text = result.data.response.storeAddress.colony
        lat = result.data.response.storeAddress.lat
        lon = result.data.response.storeAddress.lon
        binding.priceTxt.text = "₹${result.data.response.billSummary.consultationFee}"
        setUpCartProducts(result.data.response.products)
    }

    private fun showOrderTypeBs(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val onlineOrderTypeBsBinding = OnlineOrderTypeBsBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(onlineOrderTypeBsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        onlineOrderTypeBsBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        onlineOrderTypeBsBinding.paymentGatewayLl.setOnClickListener(View.OnClickListener {
            bookPharmacyProduct("online")
            bottomSheetDialog.cancel()
        })
        onlineOrderTypeBsBinding.walletLl.setOnClickListener(View.OnClickListener {
            bookPharmacyProduct("wallet")
            bottomSheetDialog.cancel()

        })
        bottomSheetDialog.show()
    }
}