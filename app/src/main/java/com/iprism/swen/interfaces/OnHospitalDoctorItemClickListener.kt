package com.iprism.swen.interfaces

import com.iprism.swen.models.hospitaldoctors.DoctorsItem


interface OnHospitalDoctorItemClickListener {

    fun onItemClicked(doctor : DoctorsItem)
}