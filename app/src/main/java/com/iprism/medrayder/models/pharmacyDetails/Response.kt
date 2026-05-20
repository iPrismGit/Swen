package com.iprism.medrayder.models.pharmacyDetails

import com.google.gson.annotations.SerializedName
import com.iprism.medrayder.models.pharmacies.MainDataItem

data class Response(

	@field:SerializedName("main_data")
	val mainData: MainData
)