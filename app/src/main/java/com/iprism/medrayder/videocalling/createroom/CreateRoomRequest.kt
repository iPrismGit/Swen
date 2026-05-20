package com.iprism.medrayderonlinedoctor.videocalling.createroom

import com.google.gson.annotations.SerializedName

data class CreateRoomRequest(

	@field:SerializedName("doctor_id")
	val doctorId: Int,

	@field:SerializedName("user_id")
	val userId: Int
)