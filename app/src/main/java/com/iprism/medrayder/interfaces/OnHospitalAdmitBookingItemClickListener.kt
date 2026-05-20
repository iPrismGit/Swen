package com.iprism.medrayder.interfaces


interface OnHospitalAdmitBookingItemClickListener {

    fun onItemClicked(id : String, bookingType : String)
    fun onCallClicked(mobile : String)
}