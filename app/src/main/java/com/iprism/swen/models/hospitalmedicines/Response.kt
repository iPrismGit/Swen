package com.iprism.swen.models.hospitalmedicines

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.pharmacyDetails.PharmacyCategory

data class Response(

	@field:SerializedName("pharmacy_categories")
	val pharmacyCategories: List<PharmacyCategory>
)