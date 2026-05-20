package com.iprism.swen.models.medlocker

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImagesItem(

	@field:SerializedName("modified_on")
	val modifiedOn: Any,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("med_locker_id")
	val medLockerId: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("delete_status")
	val deleteStatus: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("status")
	val status: String
) : Serializable