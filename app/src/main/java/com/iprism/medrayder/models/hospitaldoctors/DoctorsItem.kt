package com.iprism.medrayder.models.hospitaldoctors

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DoctorsItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("unique_id")
	val uniqueId: String,

	@field:SerializedName("online_fee")
	val onlineFee: Int,

	@field:SerializedName("sub_sub_cat_id")
	val subSubCatId: Int,

	@field:SerializedName("offline_fee")
	val offlineFee: Int,

	@field:SerializedName("sub_cat_id")
	val subCatId: Int,

	@field:SerializedName("speciality_id")
	val specialityId: Int,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("consult_type")
	val consultType: String,

	@field:SerializedName("consultations")
	val consultations: Int,

	@field:SerializedName("qualification")
	val qualification: String,

	@field:SerializedName("cat_id")
	val catId: Int,

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

	@field:SerializedName("exp")
	val exp: String,

	@field:SerializedName("hospital_name")
	val hospitalName: String,
) : Serializable