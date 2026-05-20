package com.iprism.medrayder.models.dignosticcenters

import com.google.gson.annotations.SerializedName

data class DiagnosticCentersApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)