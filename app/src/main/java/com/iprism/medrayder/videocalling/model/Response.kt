package com.iprism.kaamhainapartnerapp.activities.multiconferencequickapp.model

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("result")
	val result: Int,

	@field:SerializedName("token")
	val token: String
)