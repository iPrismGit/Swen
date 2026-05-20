package com.iprism.swen.models.onlinedoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class History(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("medicines")
	val medicines: List<MedicinesItem>,

	@field:SerializedName("notes")
	val notes: List<String>,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("speciality_id")
	val specialityId: Int,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("booking_id")
	val bookingId: String,

	@field:SerializedName("doctor_id")
	val doctorId: Int,

	@field:SerializedName("call_status")
	val callStatus: Int,

	@field:SerializedName("coupon_id")
	val couponId: Int,

	@field:SerializedName("patient_name")
	val patientName: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: Int,

	@field:SerializedName("specialization")
	val specialization: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("tests")
	val tests: List<TestsItem>,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("slot_id")
	val slotId: Int,

	@field:SerializedName("dob")
	val dob: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("blood_group")
	val bloodGroup: String,

	@field:SerializedName("consult_type")
	val consultType: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: Int,

	@field:SerializedName("family_member_id")
	val familyMemberId: Int,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

	@field:SerializedName("exp")
	val exp: String,

	@field:SerializedName("consultations")
	val consultations: String,

	@field:SerializedName("qualification")
	val qualification: String,

	@field:SerializedName("payment_type")
	val paymentType: String,

	@field:SerializedName("rating_id")
	val ratingId: String,

	@field:SerializedName("hospital_name")
	val hospitalName: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("lon")
	val lon: String
) : Serializable