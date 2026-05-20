package com.iprism.swen.models.login

import com.google.gson.annotations.SerializedName

data class LoginApiResponse(

    @field:SerializedName("response")
	val loginResponse: LoginResponse,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("status")
	val status: Boolean
)