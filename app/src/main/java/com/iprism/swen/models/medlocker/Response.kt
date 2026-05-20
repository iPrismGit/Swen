package com.iprism.swen.models.medlocker

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("med_locker")
	val medLocker: List<MedLockerItem>
)