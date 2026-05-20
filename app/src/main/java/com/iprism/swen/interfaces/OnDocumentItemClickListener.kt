package com.iprism.swen.interfaces

import com.iprism.swen.models.medlocker.MedLockerItem

interface OnDocumentItemClickListener {

    fun onItemClicked(medLockerItem : MedLockerItem)
}