package com.iprism.swen.models.register

import com.google.gson.annotations.SerializedName

data class RegisterRequest(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("coverage_category")
	val coverageCategory: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("dob")
	val dob: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("blood_group")
	val bloodGroup: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("relationship")
	val relationship: String
)