package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.hospitalambulancebookings.HistoryItem


interface OnAmbulanceBookingItemClickListener {

    fun onItemClicked(details : HistoryItem)
    fun onCallClicked(mobile : String)
    fun onTrackClicked(details : HistoryItem)
}