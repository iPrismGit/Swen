package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.swen.R
import com.iprism.swen.adapters.PharmacyProductsAdapter
import com.iprism.swen.databinding.ActivityPharmacyProductsBinding
import com.iprism.swen.databinding.ChooseOrderTypeBsBinding
import com.iprism.swen.interfaces.OnProductItemClickListener
import com.iprism.swen.models.pharmacyaddtocart.PharmacyProductAddToCartRequest
import com.iprism.swen.models.pharmacyproducts.MainDataItem
import com.iprism.swen.models.pharmacyproducts.PharmacyProductsRequest
import com.iprism.swen.repository.PharmaciesRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.getUserDetails
import com.iprism.swen.utils.hideProgress
import com.iprism.swen.utils.showProgress
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.PharmacyProductsViewModel
import com.iprism.swen.viewmodels.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PharmacyProductsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPharmacyProductsBinding
    private lateinit var viewModel: PharmacyProductsViewModel
    private var pharmacyId = ""
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
    private var searchJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPharmacyProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("pharmacyId")) {
            pharmacyId = intent.getStringExtra("pharmacyId")!!
            catId = intent.getStringExtra("catId")!!
            catName = intent.getStringExtra("catName")!!
            orderType = intent.getStringExtra("orderType")!!
            binding.catNameTxt.text = intent.getStringExtra("catName")!!
        }
        handleBack()
        handleViewCartImg()
        handleSearchLl()
        handleCrossImg()
        handleSearchEt()
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

    private fun handleSearchEt() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Optionally, debounce this call to avoid calling fetch too frequently
                searchJob?.cancel()

                searchJob = lifecycleScope.launch {
                    delay(500) // debounce time
                    val query = s.toString().trim()
                    if (query.isNotEmpty()) {
                        currentPage = 1
                        isLastPage = false
                        products.clear()
                        productsAdapter.notifyDataSetChanged()
                        getProducts()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleSearchLl() {
        binding.searchImg.setOnClickListener(View.OnClickListener {
            showKeyboard()
            binding.searchLl.visibility =  View.VISIBLE
            binding.catNameTxt.visibility =  View.GONE
            binding.optionsLl.visibility =  View.GONE
        })
    }

    private fun showKeyboard() {
        binding.searchEt.postDelayed({
            binding.searchEt.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchEt, InputMethodManager.SHOW_IMPLICIT)
        }, 100)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEt.windowToken, 0)
    }

    private fun handleCrossImg() {
        binding.crossImg.setOnClickListener(View.OnClickListener {
            hideKeyboard()
            binding.searchLl.visibility =  View.GONE
            binding.catNameTxt.visibility =  View.VISIBLE
            binding.optionsLl.visibility =  View.VISIBLE
            binding.searchEt.setText("")
            currentPage = 1
            isLastPage = false
            products.clear()
            productsAdapter.notifyDataSetChanged()
            getProducts()
        })
    }

    private fun handleViewCartImg() {
        binding.viewCartImg.setOnClickListener(View.OnClickListener {
            navigateCart("")
            /*if (orderType.equals("yes", true)) {
                showOrderTypeBs(this)
            } else {
                navigateCart("pickup")
            }*/
        })
    }

    private fun handleCartLL() {
        binding.cartLl.setOnClickListener(View.OnClickListener {
            navigateCart("")
            /*if (orderType.equals("yes", true)) {
                showOrderTypeBs(this)
            } else {
                navigateCart("pickup")
            }*/
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
                this@PharmacyProductsActivity.position = position.toString()
                addToCartProduct(product, viewType)
            }

            override fun onIncrementClicked(product: MainDataItem,  position : Int) {
                viewType = "increment"
                this@PharmacyProductsActivity.position = position.toString()
                addToCartProduct(product, viewType)
            }

            override fun onDecrementClicked(product: MainDataItem,  position : Int) {
                viewType = "decrement"
                this@PharmacyProductsActivity.position = position.toString()
                addToCartProduct(product, viewType)
            }
        })
    }

    private fun initViewModel() {
        val repository = PharmaciesRepository()
        val factory = ViewModelFactory { PharmacyProductsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[PharmacyProductsViewModel::class.java]
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
                    binding.noDataLl.visibility = View.GONE
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
        val request = PharmacyProductsRequest(
            userDetails[User.ID]!!.toInt(),
            pharmacyId.toInt(),
            catId.toInt(),
            userDetails[User.LANG].toString(),
            currentPage,
            userDetails[User.AUTH_TOKEN].toString(),
            binding.searchEt.text.toString().trim()
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.fetchPharmacyProducts(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun addToCartProduct(product : MainDataItem, viewType : String) {
        val userDetails = getUserDetails()
        val request = PharmacyProductAddToCartRequest(
            product.cartId,
            userDetails[User.ID]!!.toInt(),
            pharmacyId.toInt(),
            product.discountPrice,
            product.id,
            catId.toInt(),
            viewType,
            userDetails[User.AUTH_TOKEN].toString(),
            product.price.toString()
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, request) { req ->
            viewModel.addToCartPharmacyProduct(req)
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
        val intent = Intent(this, ProductsCartActivity::class.java)
        intent.putExtra("pharmacyId", pharmacyId)
        intent.putExtra("catId", catId)
        intent.putExtra("orderType", orderType)
        startActivity(intent)
    }
}