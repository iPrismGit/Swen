package com.iprism.medrayder.models.treatmentplaning

import com.google.gson.annotations.SerializedName

data class TreatmentPlaningRequest(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("auth_token")
	val authToken: String
)
