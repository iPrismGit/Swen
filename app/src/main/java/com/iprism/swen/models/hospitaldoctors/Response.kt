package com.iprism.swen.models.hospitaldoctors

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("doctors")
	val doctors: List<DoctorsItem>
)