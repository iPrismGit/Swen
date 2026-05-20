package com.iprism.medrayder.models.resendotp

import com.google.gson.annotations.SerializedName

data class ResendOtpRequest(

	@field:SerializedName("mobile")
	val mobile: String
)