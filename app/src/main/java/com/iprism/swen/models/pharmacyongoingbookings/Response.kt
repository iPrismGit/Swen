package com.iprism.swen.models.pharmacyongoingbookings

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.hospitalmedicneongoing.BookingsItem

data class Response(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("orders")
	val orders: List<BookingsItem>
)