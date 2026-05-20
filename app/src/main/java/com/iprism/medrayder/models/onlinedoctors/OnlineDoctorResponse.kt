package com.iprism.medrayder.models.onlinedoctors

import com.google.gson.annotations.SerializedName

data class OnlineDoctorResponse(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("doctors")
	val doctors: List<DoctorsItem>
)