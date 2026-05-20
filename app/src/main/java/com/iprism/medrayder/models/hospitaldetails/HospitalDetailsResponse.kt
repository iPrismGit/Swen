package com.iprism.medrayder.models.hospitaldetails

import com.google.gson.annotations.SerializedName

data class HospitalDetailsResponse(

	@field:SerializedName("main_data")
	val mainData: MainDataItem
)