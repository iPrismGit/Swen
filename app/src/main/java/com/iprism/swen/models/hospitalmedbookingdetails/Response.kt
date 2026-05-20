package com.iprism.swen.models.hospitalmedbookingdetails

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("booking_details")
	val bookingDetails: BookingDetails
)