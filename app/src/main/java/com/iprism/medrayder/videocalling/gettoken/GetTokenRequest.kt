package com.iprism.medrayderonlinedoctor.videocalling.gettoken

import com.google.gson.annotations.SerializedName

data class GetTokenRequest(

	@field:SerializedName("room_id")
	val roomId: String,

	@field:SerializedName("doctor_id")
	val doctorId: Int,

	@field:SerializedName("user_id")
	val userId: Int
)