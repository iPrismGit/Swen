package com.iprism.medrayder.models.resendotp

import com.google.gson.annotations.SerializedName

data class ResendOtpResponse(

	@field:SerializedName("otp")
	val otp: String
)