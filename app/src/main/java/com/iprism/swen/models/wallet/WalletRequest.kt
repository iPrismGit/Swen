package com.iprism.swen.models.wallet

import com.google.gson.annotations.SerializedName

data class WalletRequest(

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("auth_token")
	val authToken: String
)