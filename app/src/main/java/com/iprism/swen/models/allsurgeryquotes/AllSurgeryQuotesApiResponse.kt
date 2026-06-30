package com.iprism.swen.models.allsurgeryquotes

import com.google.gson.annotations.SerializedName

data class AllSurgeryQuotesApiResponse(

	@field:SerializedName("response")
	val response: ArrayList<ResponseItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class ResponseItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("main_data_id")
	val mainDataId: Int
)
