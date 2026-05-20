package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.onlinedoctorbookingdetails.DatesItem
import com.iprism.medrayder.models.onlinedoctors.DoctorsItem

interface OnDateItemClickListener {

    fun onItemClicked(date : DatesItem)
}