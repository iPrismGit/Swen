package com.iprism.medrayder.models.wallet

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("wallet_amount")
	val walletAmount: String,
)