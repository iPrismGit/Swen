package com.iprism.swen.models.diagnostictimings

import com.google.gson.annotations.SerializedName

data class DiagnosticSlotsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)