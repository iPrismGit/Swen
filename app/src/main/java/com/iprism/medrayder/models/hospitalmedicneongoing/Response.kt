package com.iprism.medrayder.models.hospitalmedicneongoing

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("bookings")
	val bookings: List<BookingsItem>
)