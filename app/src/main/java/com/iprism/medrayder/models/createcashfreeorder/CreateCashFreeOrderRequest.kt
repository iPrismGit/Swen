package com.iprism.medrayder.models.createcashfreeorder

import com.google.gson.annotations.SerializedName

data class CreateCashFreeOrderRequest(

	@field:SerializedName("amount")
	val amount: String,

	@field:SerializedName("user_id")
	val userId: Int
)
