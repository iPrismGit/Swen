package com.iprism.swen.models.surgerysymptoms

import com.google.gson.annotations.SerializedName

data class SurgerySymptomsDoctorRequest(

	@field:SerializedName("symptom_id")
	val symptomId: String,

	@field:SerializedName("search")
	val search: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("lat")
	val lat: String
)
