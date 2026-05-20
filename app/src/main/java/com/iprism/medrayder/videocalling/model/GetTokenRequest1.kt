package com.iprism.kaamhainapartnerapp.activities.multiconferencequickapp.model

import com.google.gson.annotations.SerializedName

data class GetTokenRequest1(

	@field:SerializedName("room_id")
	val roomId: String,

	@field:SerializedName("emp_id")
	val empId: String,

	@field:SerializedName("user_id")
	val userId: String
)