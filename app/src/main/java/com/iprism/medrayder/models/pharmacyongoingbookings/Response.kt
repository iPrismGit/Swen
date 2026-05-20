package com.iprism.medrayder.models.pharmacyongoingbookings

import com.google.gson.annotations.SerializedName
import com.iprism.medrayder.models.hospitalmedicneongoing.BookingsItem

data class Response(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("orders")
	val orders: List<BookingsItem>
)