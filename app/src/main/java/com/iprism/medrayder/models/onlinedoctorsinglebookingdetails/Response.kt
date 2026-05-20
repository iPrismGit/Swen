package com.iprism.medrayder.models.onlinedoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("history")
	val history: History
)