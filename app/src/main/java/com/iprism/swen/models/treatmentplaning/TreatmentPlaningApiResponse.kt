package com.iprism.swen.models.treatmentplaning

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TreatmentPlaningApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Response(

	@field:SerializedName("treatment_planning")
	val treatmentPlanning: List<TreatmentPlanningItem>
)

data class TreatmentPlanningItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
) : Serializable
