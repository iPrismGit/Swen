package com.iprism.swen.models.labtestbookingdetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReportsItem(

	@field:SerializedName("image")
	val image: String
) : Serializable
