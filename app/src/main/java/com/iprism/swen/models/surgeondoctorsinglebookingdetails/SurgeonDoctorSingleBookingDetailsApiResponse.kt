package com.iprism.swen.models.surgeondoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.History

data class SurgeonDoctorSingleBookingDetailsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Response(

	@field:SerializedName("history")
	val history: History
)
