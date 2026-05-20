package com.iprism.swen.models.hospitalambulancebookings

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HistoryItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("trip_status")
	val tripStatus: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("open_time")
	val openTime: String,

	@field:SerializedName("ambulance_driver_mobile")
	val ambulanceDriverMobile: String,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

	@field:SerializedName("close_time")
	val closeTime: String,

	@field:SerializedName("hospital_id")
	val hospitalId: Int,

	@field:SerializedName("ambulance_driver_image")
	val ambulanceDriverImage: String,

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("accept_status")
	val acceptStatus: String,

	@field:SerializedName("ambulance_driver_name")
	val ambulanceDriverName: String,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("vehicle_number")
	val vehicleNumber: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("tagline")
	val tagline: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("hospital_name")
	val hospitalName: String,

	@field:SerializedName("pick_up_location")
	val pickUpLocation: PickupLocation,
) : Serializable