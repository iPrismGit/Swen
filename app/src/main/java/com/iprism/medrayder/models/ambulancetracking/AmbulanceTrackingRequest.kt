package com.iprism.medrayder.models.ambulancetracking

import com.google.gson.annotations.SerializedName

data class AmbulanceTrackingRequest(

	@field:SerializedName("booking_id")
	val bookingId: String
)
