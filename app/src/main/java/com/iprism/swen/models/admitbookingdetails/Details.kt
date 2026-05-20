package com.iprism.swen.models.admitbookingdetails

import com.google.gson.annotations.SerializedName

data class Details(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("reports")
	val reports: List<Any>,

	@field:SerializedName("open_time")
	val openTime: String,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

	@field:SerializedName("close_time")
	val closeTime: String,

	@field:SerializedName("hospital_id")
	val hospitalId: Int,

	@field:SerializedName("pr_mobile")
	val prMobile: String	,

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("pr_name")
	val prName: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("family_members")
	val familyMembers: List<FamilyMembersItem>,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("pr_image")
	val prImage: String,

	@field:SerializedName("tagline")
	val tagLine: String
)