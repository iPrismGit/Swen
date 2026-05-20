package com.iprism.swen.models.onlinedoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TestsItem(

	@field:SerializedName("test")
	val test: String,

	@field:SerializedName("test_instruction")
	val testInstruction: String
) : Serializable