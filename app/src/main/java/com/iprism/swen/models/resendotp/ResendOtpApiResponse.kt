package com.iprism.swen.models.resendotp

import com.google.gson.annotations.SerializedName

data class ResendOtpApiResponse(

    @field:SerializedName("response")
	val resendOtpResponse: ResendOtpResponse,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("status")
	val status: Boolean
)