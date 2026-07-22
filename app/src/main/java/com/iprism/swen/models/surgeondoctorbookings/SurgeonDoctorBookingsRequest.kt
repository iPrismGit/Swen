package com.iprism.swen.models.surgeondoctorbookings

import com.google.gson.annotations.SerializedName

data class SurgeonDoctorBookingsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int
)
