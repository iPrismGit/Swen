package com.iprism.medrayderonlinedoctor.videocalling.createroom

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("result")
	val result: Int,

	@field:SerializedName("room")
	val room: Room
)