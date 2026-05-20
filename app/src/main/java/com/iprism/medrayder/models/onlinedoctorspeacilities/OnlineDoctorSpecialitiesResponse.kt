package com.iprism.medrayder.models.onlinedoctorspeacilities

import com.google.gson.annotations.SerializedName

data class OnlineDoctorSpecialitiesResponse(

	@field:SerializedName("specialities")
	val specialities: List<SpecialitiesItem>
)