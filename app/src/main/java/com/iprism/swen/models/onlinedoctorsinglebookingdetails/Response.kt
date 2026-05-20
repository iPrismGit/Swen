package com.iprism.swen.models.onlinedoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("history")
	val history: History
)