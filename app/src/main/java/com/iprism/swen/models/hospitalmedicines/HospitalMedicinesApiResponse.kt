package com.iprism.swen.models.hospitalmedicines

import com.google.gson.annotations.SerializedName

data class HospitalMedicinesApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)