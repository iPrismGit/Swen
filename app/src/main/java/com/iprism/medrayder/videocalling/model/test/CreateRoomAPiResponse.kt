package com.iprism.khamhaina.activities.multiconferencequickapp.model.test

import com.google.gson.annotations.SerializedName
import com.iprism.kaamhainapartnerapp.activities.multiconferencequickapp.model.test.Response

data class CreateRoomAPiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)