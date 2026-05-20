package com.iprism.medrayder.models.userdropdowns

import com.google.gson.annotations.SerializedName

data class UserDropDownRequest(

	@field:SerializedName("language")
	val language: String,
)