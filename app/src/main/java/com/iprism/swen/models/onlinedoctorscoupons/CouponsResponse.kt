package com.iprism.swen.models.onlinedoctorscoupons

import com.google.gson.annotations.SerializedName

data class CouponsResponse(

	@field:SerializedName("coupons")
	val coupons: List<CouponsItem>
)