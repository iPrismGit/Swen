package com.iprism.medrayder.models.checkpackage

import com.google.gson.annotations.SerializedName

data class CheckPaymentRequest(

	@field:SerializedName("order_id")
	val orderId: String
)
