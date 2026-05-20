package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.medlocker.MedLockerItem

interface OnDocumentItemClickListener {

    fun onItemClicked(medLockerItem : MedLockerItem)
}