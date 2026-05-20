package com.iprism.swen.models.userdropdowns

import com.google.gson.annotations.SerializedName

data class UserDropDownRequest(

	@field:SerializedName("language")
	val language: String,
)