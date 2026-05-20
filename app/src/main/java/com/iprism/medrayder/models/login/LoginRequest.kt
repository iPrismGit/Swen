package com.iprism.medrayder.models.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("otp_status")
	val otpStatus: String,

	@field:SerializedName("player_id")
	val token: String,

	@field:SerializedName("referral_code")
	val referralCode: String
)