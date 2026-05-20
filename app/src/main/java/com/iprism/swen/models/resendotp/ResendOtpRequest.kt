package com.iprism.swen.models.resendotp

import com.google.gson.annotations.SerializedName

data class ResendOtpRequest(

	@field:SerializedName("mobile")
	val mobile: String
)