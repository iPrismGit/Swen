package com.iprism.kaamhainapartnerapp.activities.multiconferencequickapp.model.test

import com.google.gson.annotations.SerializedName
import com.iprism.kaamhainapartnerapp.activities.multiconferencequickapp.model.test.Room

data class Response(

	@field:SerializedName("result")
	val result: Int,

	@field:SerializedName("room")
	val room: Room
)