package com.iprism.medrayder.models.onlinedoctorsinglebookingdetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MedicinesItem(

	@field:SerializedName("medicine")
	val medicine: String,

	@field:SerializedName("medicine_time")
	val medicineTime: String
) : Serializable