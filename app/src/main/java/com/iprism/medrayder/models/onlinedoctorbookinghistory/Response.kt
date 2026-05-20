package com.iprism.medrayder.models.onlinedoctorbookinghistory

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("history")
	val history: ArrayList<HistoryItem>
)