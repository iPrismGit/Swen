package com.iprism.medrayder.models.hospitalmedicines

import com.google.gson.annotations.SerializedName
import com.iprism.medrayder.models.pharmacyDetails.PharmacyCategory

data class Response(

	@field:SerializedName("pharmacy_categories")
	val pharmacyCategories: List<PharmacyCategory>
)