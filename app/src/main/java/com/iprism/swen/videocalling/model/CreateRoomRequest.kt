package com.iprism.kaamhainapartnerapp.activities.multiconferencequickapp.model

import com.google.gson.annotations.SerializedName

data class CreateRoomRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("emp_id")
	val empId: Int
)