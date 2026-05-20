package com.iprism.swen.models.diagnostictests

import com.google.gson.annotations.SerializedName

data class DiagnosticTestsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)