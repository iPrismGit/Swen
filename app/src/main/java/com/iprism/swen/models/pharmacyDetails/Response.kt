package com.iprism.swen.models.pharmacyDetails

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("main_data")
	val mainData: MainData
)