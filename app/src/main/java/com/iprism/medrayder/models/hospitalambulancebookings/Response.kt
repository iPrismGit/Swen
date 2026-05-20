package com.iprism.medrayder.models.hospitalambulancebookings

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("history")
	val history: List<HistoryItem>
)