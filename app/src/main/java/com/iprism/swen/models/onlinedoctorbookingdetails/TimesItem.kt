package com.iprism.swen.models.onlinedoctorbookingdetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TimesItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("session")
	val session: String,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("format_date")
	val formatDate: String
) : Serializable