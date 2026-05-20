package com.iprism.medrayder.models.diagnostictests

import com.google.gson.annotations.SerializedName

data class DiagnosticTestsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("diagnostic_id")
	val diagnosticId: Int
)