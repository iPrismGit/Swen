package com.iprism.medrayder.models.labtests

import com.google.gson.annotations.SerializedName

data class LabTestsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("lab_test_id")
	val labTestId: Int
)