package com.iprism.medrayder.models.admitbookingdetails

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("details")
	val details: Details
)