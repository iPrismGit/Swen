package com.iprism.swen.models.diagnostictimings

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Price(

	@field:SerializedName("discont_price")
	val discontPrice: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("patient_count")
	val patientCount: Int
) : Serializable