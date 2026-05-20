package com.iprism.swen.models.hospitaldiagnosticprescriptionbooking

import com.google.gson.annotations.SerializedName

data class Address(

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("lat")
	val lat: String
)