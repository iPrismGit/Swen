package com.iprism.swen.interfaces

import com.iprism.swen.models.onlinedoctors.DoctorsItem

interface OnDoctorItemClickListener {

    fun onItemClicked(doctor : DoctorsItem)
}