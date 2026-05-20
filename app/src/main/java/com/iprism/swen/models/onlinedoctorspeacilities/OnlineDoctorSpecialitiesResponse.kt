package com.iprism.swen.models.onlinedoctorspeacilities

import com.google.gson.annotations.SerializedName

data class OnlineDoctorSpecialitiesResponse(

	@field:SerializedName("specialities")
	val specialities: List<SpecialitiesItem>
)