package com.iprism.medrayder.models.pharmacyDetails

import com.google.gson.annotations.SerializedName

data class PharmacyCategory(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)