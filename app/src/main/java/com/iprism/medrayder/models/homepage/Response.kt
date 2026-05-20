package com.iprism.medrayder.models.homepage

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("sub_categories")
	val subCategories: List<SubCategoriesItem>,

	@field:SerializedName("pharmacy_categories")
	val pharmacyCategories: List<PharmacyCategoriesItem>,

	@field:SerializedName("lab_test_banners")
	val labTestBanners: List<CategoriesItem>,

	@field:SerializedName("health_card_status")
	val healthCardStatus: Int,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("count")
	val count: String,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem>,

	@field:SerializedName("banners")
	val banners: List<BannersItem>
)