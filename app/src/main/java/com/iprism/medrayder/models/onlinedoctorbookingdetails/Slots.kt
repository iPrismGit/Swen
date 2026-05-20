package com.iprism.medrayder.models.onlinedoctorbookingdetails

import com.google.gson.annotations.SerializedName

data class Slots(

	@field:SerializedName("afternoon")
	val afternoon: List<TimesItem>,

	@field:SerializedName("evening")
	val evening: List<TimesItem>,

	@field:SerializedName("morning")
	val morning: List<TimesItem>
)