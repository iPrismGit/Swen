package com.iprism.medrayder.models.hospitalmedbookingdetails

import com.google.gson.annotations.SerializedName

data class BookingDetails(

	@field:SerializedName("consultation_fee")
	val consultationFee: Int,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("user_image")
	val userImage: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("address_id")
	val addressId: Int,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("products")
	val products: List<ProductsItem>,

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("accept_status")
	val acceptStatus: String,

	@field:SerializedName("payment_type")
	val paymentType: String,

	@field:SerializedName("coupon_id")
	val couponId: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("cat_id")
	val catId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("booking_type")
	val bookingType: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("order_type")
	val orderType: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: Int,

	@field:SerializedName("hospital_name")
	val hospitalName: String
)