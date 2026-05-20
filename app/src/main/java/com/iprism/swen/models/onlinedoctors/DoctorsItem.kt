package com.iprism.swen.models.onlinedoctors

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DoctorsItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("unique_id")
	val uniqueId: String,

	@field:SerializedName("speciality_id")
	val specialityId: String,

	@field:SerializedName("fee")
	val fee: String,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("consultations")
	val consultations: String,

	@field:SerializedName("qualification")
	val qualification: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("total_reviews")
	val totalReviews: Int,

	@field:SerializedName("specialization")
	val specialization: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("exp")
	val exp: String
) : Serializable