package com.iprism.swen.models.labtests

import com.google.gson.annotations.SerializedName

data class LabTestsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)