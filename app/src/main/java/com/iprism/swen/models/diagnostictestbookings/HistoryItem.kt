package com.iprism.swen.models.diagnostictestbookings

import com.google.gson.annotations.SerializedName

data class HistoryItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("open_time")
	val openTime: String,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

	@field:SerializedName("close_time")
	val closeTime: String,

	@field:SerializedName("packages")
	val packages: Packages,

	@field:SerializedName("diagnostic_id")
	val diagnosticId: Int,

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("slot_id")
	val slotId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("booking_type")
	val bookingType: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("time")
	val time: String
)