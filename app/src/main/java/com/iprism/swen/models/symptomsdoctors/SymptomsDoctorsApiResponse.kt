package com.iprism.swen.models.symptomsdoctors

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SymptomsDoctorsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Response(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("doctors")
	val doctors: List<DoctorsItem>
)

data class DoctorsItem(

	@field:SerializedName("symptom_id")
	val symptomId: Int,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("unique_id")
	val uniqueId: String,

	@field:SerializedName("symptom_name")
	val symptomName: String,

	@field:SerializedName("speciality_id")
	val specialityId: Int,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("consultations")
	val consultations: Int,

	@field:SerializedName("qualification")
	val qualification: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("total_reviews")
	val totalReviews: Int,

	@field:SerializedName("specialization")
	val specialization: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("consult_type")
	val consultType: String,

	@field:SerializedName("exp")
	val exp: Int,

	@field:SerializedName("hospital_name")
	val hospitalName: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("languages")
	val languages: String
) : Serializable

data class TotalPagesItem(

	@field:SerializedName("page")
	val page: Int
)

data class Pagination(

	@field:SerializedName("limit")
	val limit: Int,

	@field:SerializedName("total_pages")
	val totalPages: List<TotalPagesItem>,

	@field:SerializedName("current_page")
	val currentPage: Int
)
