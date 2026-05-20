package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.diagnostictimings.Price

interface OnPatientPriceItemClickListener {

    fun onItemClicked(price : Price)
}