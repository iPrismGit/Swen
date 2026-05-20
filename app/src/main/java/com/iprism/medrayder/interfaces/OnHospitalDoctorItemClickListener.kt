package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.hospitaldoctors.DoctorsItem


interface OnHospitalDoctorItemClickListener {

    fun onItemClicked(doctor : DoctorsItem)
}