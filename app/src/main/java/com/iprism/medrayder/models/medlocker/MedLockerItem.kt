package com.iprism.medrayder.models.medlocker

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MedLockerItem(

	@field:SerializedName("modified_on")
	val modifiedOn: Any,

	@field:SerializedName("images")
	val images: ArrayList<ImagesItem>,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("delete_status")
	val deleteStatus: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("alert_date")
	val alertDate: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("status")
	val status: Int
) : Serializable