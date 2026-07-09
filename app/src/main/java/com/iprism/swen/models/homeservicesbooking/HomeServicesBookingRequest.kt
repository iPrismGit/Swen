package com.iprism.swen.models.homeservicesbooking

import com.google.gson.annotations.SerializedName

data class HomeServicesBookingRequest(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("reason")
	val reason: String,

	@field:SerializedName("image")
	val image: List<String>,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("sub_cat_id")
	val subCatId: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("view_type")
	val viewType: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("dob")
	val dob: String,

	@field:SerializedName("cat_id")
	val catId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("family_member_id")
	val familyMemberId: String
)
