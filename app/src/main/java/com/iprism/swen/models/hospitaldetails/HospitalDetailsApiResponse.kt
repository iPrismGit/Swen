package com.iprism.swen.models.hospitaldetails

import com.google.gson.annotations.SerializedName

data class HospitalDetailsApiResponse(

    @field:SerializedName("response")
	val hospitalDetailsResponse: HospitalDetailsResponse,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("status")
	val status: Boolean
)