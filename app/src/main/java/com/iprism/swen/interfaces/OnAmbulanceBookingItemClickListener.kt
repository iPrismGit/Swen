package com.iprism.swen.interfaces

import com.iprism.swen.models.hospitalambulancebookings.HistoryItem


interface OnAmbulanceBookingItemClickListener {

    fun onItemClicked(details : HistoryItem)
    fun onCallClicked(mobile : String)
    fun onTrackClicked(details : HistoryItem)
}