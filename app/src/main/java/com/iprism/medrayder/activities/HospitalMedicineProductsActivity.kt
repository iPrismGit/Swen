package com.iprism.medrayder.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.medrayder.R
import com.iprism.medrayder.adapters.PharmacyProductsAdapter
import com.iprism.medrayder.databinding.ActivityPharmacyProductsBinding
import com.iprism.medrayder.databinding.ChooseOrderTypeBsBinding
import com.iprism.medrayder.interfaces.OnProductItemClickListener
import com.iprism.medrayder.models.hospitalmedicineaddtocart.HospitalMedineProductsAddToCartRequest
import com.iprism.medrayder.models.hospitalmedicineproducts.HospitalMedicineProductsRequest
import com.iprism.medrayder.models.pharmacyaddtocart.PharmacyProductAddToCartRequest
import com.iprism.medrayder.models.pharmacyproducts.MainDataItem
import com.iprism.medrayder.repository.HospitalsMedicineRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.hideProgress
import com.iprism.medrayder.utils.showProgress
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.HospitalMedicineProductsViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory

class HospitalMedicineProductsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPharmacyProductsBinding
    private lateinit var viewModel: HospitalMedicineProductsViewModel
    private var hospitalId = ""
    private var orderType = ""
    private var catId = ""
    private var catName = ""
    private var viewType = ""
    private var position = ""
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var products = mutableListOf<MainDataItem>()
    private lateinit var productsAdapter: PharmacyProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmacyProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("hospitalId")) {
            hospitalId = intent.getStringExtra("hospitalId")!!
            catId = intent.getStringExtra("catId")!!
            catName = intent.getStringExtra("catName")!!
            orderType = intent.getStringExtra("orderType")!!
            binding.catNameTxt.text = intent.getStringExtra("catName")!!
        }
        handleBack()
        handleViewCartImg()
        handleCartLL()
        setUpProducts()
        initViewModel()
        observeProductsResponse()
        observeAddToCartResponse()
    }

    override fun onResume() {
        super.onResume()
        currentPage = 1
        isLastPage = false
        products.clear()
        productsAdapter.notifyDataSetChanged()
        getProducts()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleViewCartImg() {
        binding.viewCartImg.setOnClickListener(View.OnClickListener {
            if (orderType.equals("yes", true)) {
                showOrderTypeBs(this)
            } else {
                navigateCart("pickup")
            }
        })
    }

    private fun handleCartLL() {
        binding.cartLl.setOnClickListener(View.OnClickListener {
            if (orderType.equals("yes", true)) {
                showOrderTypeBs(this)
            } else {
                navigateCart("pickup")
            }
        })
    }

    private fun setUpProducts() {
        productsAdapter = PharmacyProductsAdapter(products as ArrayList<MainDataItem?>)
        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.productRv.apply {
            layoutManager = gridLayoutManager
            adapter = productsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy <= 0) return
                    val totalItemCount = gridLayoutManager.itemCount
                    val lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition()
                    if (!isLoading && !isLastPage && lastVisibleItemPosition >= totalItemCount - 2) {
                        loadMoreProducts()
                    }
                }
            })
        }
        productsAdapter.setOnProductItemClickListener(object : OnProductItemClickListener{
            override fun onAddClicked(product: MainDataItem, position : Int) {
                viewType = "add"
                this@HospitalMedicineProductsActivity.position = position.toString()
                addToCartProduct(product, viewType)
            }

            override fun onIncrementClicked(product: MainDataItem,  position : Int) {
                viewType = "increment"
                this@HospitalMedicineProductsActivity.position = position.toString()
                addToCartProduct(product, viewType)
            }

            override fun onDecrementClicked(product: MainDataItem,  position : Int) {
                viewType = "decrement"
                this@HospitalMedicineProductsActivity.position = position.toString()
                addToCartProduct(product, viewType)
            }
        })
    }

    private fun initViewModel() {
        val repository = HospitalsMedicineRepository()
        val factory = ViewModelFactory { HospitalMedicineProductsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HospitalMedicineProductsViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeProductsResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    productsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.mainData
                    if (newBookings.isNotEmpty()) {
                        products.addAll(newBookings)
                        productsAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                    if (result.data.response.count == 0) {
                        binding.cartLl.visibility = View.GONE
                        binding.cartCountCv.visibility = View.GONE
                        binding.viewCartImg.visibility = View.GONE
                    } else {
                        binding.viewCartImg.visibility = View.VISIBLE
                        binding.cartLl.visibility = View.VISIBLE
                        binding.cartCountCv.visibility = View.VISIBLE
                        binding.itemsPriceTxt.text = "${result.data.response.count} ${getString(R.string.items)} | ₹ ${result.data.response.fee}"
                        binding.cartCountTxt.text = result.data.response.count.toString()
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    productsAdapter.removeLoadingFooter()
                    showToast(result.message)
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                        binding.viewCartImg.visibility = View.GONE
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
                    if (result.data.response.count == 0) {
                        binding.cartLl.visibility = View.GONE
                        binding.cartCountCv.visibility = View.GONE
                        binding.viewCartImg.visibility = View.GONE
                    } else {
                        binding.viewCartImg.visibility = View.VISIBLE
                        binding.cartLl.visibility = View.VISIBLE
                        binding.cartCountCv.visibility = View.VISIBLE
                        binding.itemsPriceTxt.text = "${result.data.response.count} ${getString(R.string.items)} | ₹ ${result.data.response.fee}"
                        binding.cartCountTxt.text = result.data.response.count.toString()
                    }
                    if (viewType == "increment" || viewType == "add") {
                        productsAdapter.addProductCount(position.toInt())
                    } else if(viewType == "decrement") {
                        productsAdapter.decreaseProductCount(position.toInt())
                    }
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun getProducts() {
        val userDetails = getUserDetails()
        val request = HospitalMedicineProductsRequest(
            userDetails[User.ID]!!.toInt(),
            catId.toInt(),
            userDetails[User.LANG].toString(),
            currentPage,
            userDetails[User.AUTH_TOKEN].toString(),
            hospitalId
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchHospitalMedicineProducts(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun addToCartProduct(product : MainDataItem, viewType : String) {
        val userDetails = getUserDetails()
        val request = HospitalMedineProductsAddToCartRequest(
            product.price,
            product.cartId,
            userDetails[User.ID]!!.toInt(),
            product.discountPrice,
            product.id,
            catId.toInt(),
            viewType,
            userDetails[User.AUTH_TOKEN].toString(),
            hospitalId
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.addToCartProduct(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreProducts() {
        isLoading = true
        currentPage += 1
        productsAdapter.showLoadingFooter()
        getProducts()
    }

    private fun showOrderTypeBs(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val chooseOrderTypeBsBinding = ChooseOrderTypeBsBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(chooseOrderTypeBsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        chooseOrderTypeBsBinding.crossImg.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.cancel()
        })
        chooseOrderTypeBsBinding.pickUpOrderLl.setOnClickListener(View.OnClickListener {
            navigateCart("pickup")
            bottomSheetDialog.cancel()
        })
        chooseOrderTypeBsBinding.homeDeliveryLl.setOnClickListener(View.OnClickListener {
            navigateCart("home_delivery")
            bottomSheetDialog.cancel()
        })
        bottomSheetDialog.show()
    }

    private fun navigateCart(orderType : String) {
        val intent = Intent(this, HospitalMedicineCartActivity::class.java)
        intent.putExtra("hospitalId", hospitalId)
        intent.putExtra("catId", catId)
        intent.putExtra("orderType", orderType)
        startActivity(intent)
    }
}