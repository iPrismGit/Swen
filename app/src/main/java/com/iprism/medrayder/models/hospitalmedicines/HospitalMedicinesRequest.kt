package com.iprism.medrayder.models.hospitalmedicines

import com.google.gson.annotations.SerializedName

data class HospitalMedicinesRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("auth_token")
	val authToken: String
)