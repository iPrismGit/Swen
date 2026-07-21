package com.iprism.swen.models.allsurgeonsymptoms

import com.google.gson.annotations.SerializedName

data class AllSurgeonSymptomsRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("auth_token")
	val authToken: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("view_type")
	val viewType: String
)
