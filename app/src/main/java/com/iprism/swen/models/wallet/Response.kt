package com.iprism.swen.models.wallet

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("wallet_amount")
	val walletAmount: String,
)