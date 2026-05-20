package com.iprism.swen.models.admitbookingdetails

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("details")
	val details: Details
)