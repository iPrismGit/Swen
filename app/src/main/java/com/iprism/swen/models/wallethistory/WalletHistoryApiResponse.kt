package com.iprism.swen.models.wallethistory

import com.google.gson.annotations.SerializedName

data class WalletHistoryApiResponse(

	@field:SerializedName("response")
	val response: List<ResponseItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)