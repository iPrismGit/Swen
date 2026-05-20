package com.iprism.swen.models.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("modified_on")
	val modifiedOn: Any,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("executive_id")
	val executiveId: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("health_card_status")
	val healthCardStatus: String,

	@field:SerializedName("player_id")
	val playerId: String,

	@field:SerializedName("coverage_category")
	val coverageCategory: String,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("dob")
	val dob: String,

	@field:SerializedName("delete_status")
	val deleteStatus: Int,

	@field:SerializedName("reg_status")
	val regStatus: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("blood_group")
	val bloodGroup: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("user_addresses_status")
	val userAddressesStatus: String,

	@field:SerializedName("family_member_status")
	val familyMemberStatus: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("status")
	val status: Int,

	@field:SerializedName("otp")
	val otp: String
)