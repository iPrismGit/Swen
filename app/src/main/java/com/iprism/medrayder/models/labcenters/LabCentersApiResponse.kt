package com.iprism.medrayder.models.labcenters

import com.google.gson.annotations.SerializedName

data class LabCentersApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)