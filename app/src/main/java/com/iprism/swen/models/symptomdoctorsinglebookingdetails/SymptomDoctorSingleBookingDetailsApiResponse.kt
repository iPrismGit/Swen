package com.iprism.swen.models.symptomdoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.MedicinesItem
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.TestsItem
import java.io.Serializable

data class SymptomDoctorSingleBookingDetailsApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class History(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("symptom_id")
	val symptomId: Int,

	@field:SerializedName("coupon_percentage")
	val couponPercentage: Int,

	@field:SerializedName("medicines")
	val medicines: List<MedicinesItem>,

	@field:SerializedName("notes")
	val notes: List<String>,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("symptom_name")
	val symptomName: String,

	@field:SerializedName("speciality_id")
	val specialityId: Int,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("lon")
	val lon: String,

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

	@field:SerializedName("exp")
	val exp: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("hospital_name")
	val hospitalName: String,

	@field:SerializedName("consultation_fee")
	val consultationFee: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("mobile")
	val mobile: String,

	@field:SerializedName("booking_status")
	val bookingStatus: String,

	@field:SerializedName("consultations")
	val consultations: Int,

	@field:SerializedName("main_data_id")
	val mainDataId: Int,

	@field:SerializedName("qualification")
	val qualification: String,

	@field:SerializedName("payment_type")
	val paymentType: String,

	@field:SerializedName("tests")
	val tests: List<TestsItem>,

	@field:SerializedName("rating_id")
	val ratingId: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("slot_id")
	val slotId: Int,

	@field:SerializedName("dob")
	val dob: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("specialization")
	val specialization: String,

	@field:SerializedName("blood_group")
	val bloodGroup: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("consult_type")
	val consultType: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("coupon_discount")
	val couponDiscount: String,

	@field:SerializedName("family_member_id")
	val familyMemberId: Int
) : Serializable

data class Response(

	@field:SerializedName("history")
	val history: History
)
