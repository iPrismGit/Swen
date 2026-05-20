package com.iprism.swen.models.pharmacyproductcart

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("bill_summary")
	val billSummary: BillSummary,

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("store_address")
	val storeAddress: Address,

	@field:SerializedName("receipt_details")
	val receiptDetails: ReceiptDetails,

	@field:SerializedName("products")
	val products: List<ProductsItem>,

	@field:SerializedName("wallet_amount")
	val walletAmount: String,

	@field:SerializedName("subscription_status")
	val subscriptionStatus: String,

	@field:SerializedName("order_type")
	val orderType: String,
)