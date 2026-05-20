package com.iprism.swen.models.onlinedoctorscoupons

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CouponsItem(

	@field:SerializedName("percentage")
	val percentage: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int
) : Serializable