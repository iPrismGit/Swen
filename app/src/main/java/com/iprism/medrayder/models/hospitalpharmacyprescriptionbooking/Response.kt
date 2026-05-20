package com.iprism.medrayder.models.hospitalpharmacyprescriptionbooking

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("store_address")
	val storeAddress: Address,

	@field:SerializedName("receipt_details")
	val receiptDetails: ReceiptDetails
)