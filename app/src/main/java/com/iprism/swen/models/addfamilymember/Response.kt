package com.iprism.swen.models.addfamilymember

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("modified_on")
	val modifiedOn: Any,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("executive_id")
	val executiveId: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("mobile")
	val mobile: Long,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("coverage_category")
	val coverageCategory: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("dob")
	val dob: String,

	@field:SerializedName("delete_status")
	val deleteStatus: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("blood_group")
	val bloodGroup: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("status")
	val status: Int
)