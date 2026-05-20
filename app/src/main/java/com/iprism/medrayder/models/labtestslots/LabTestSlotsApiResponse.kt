package com.iprism.medrayder.models.labtestslots

import com.google.gson.annotations.SerializedName

data class LabTestSlotsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)