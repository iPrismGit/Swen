package com.iprism.swen.models.subscription

import com.google.gson.annotations.SerializedName

data class HealthCardsItem(

	@field:SerializedName("duration")
	val duration: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("discount_price")
	val discountPrice: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)