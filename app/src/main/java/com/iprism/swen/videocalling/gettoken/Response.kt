package com.iprism.medrayderonlinedoctor.videocalling.gettoken

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("result")
	val result: Int,

	@field:SerializedName("token")
	val token: String
)