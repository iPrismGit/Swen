package com.iprism.swen.models.admitbookingdetails

import com.google.gson.annotations.SerializedName

data class FamilyMembersItem(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("dob")
	val dob: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)