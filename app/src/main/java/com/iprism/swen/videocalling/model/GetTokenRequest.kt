package com.iprism.kaamhainapartnerapp.activities.multiconferencequickapp.model

import com.google.gson.annotations.SerializedName

data class GetTokenRequest(

	@field:SerializedName("room_id")
	val roomId: String
)