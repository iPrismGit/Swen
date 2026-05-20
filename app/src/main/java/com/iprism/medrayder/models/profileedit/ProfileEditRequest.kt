package com.iprism.medrayder.models.profileedit

import com.google.gson.annotations.SerializedName

data class ProfileEditRequest(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("gender")
	val gender: String,

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

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("family_member_id")
	val familyMemberId: Int
)
