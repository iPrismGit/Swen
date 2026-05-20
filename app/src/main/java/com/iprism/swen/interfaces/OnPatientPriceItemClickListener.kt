package com.iprism.swen.interfaces

import com.iprism.swen.models.diagnostictimings.Price

interface OnPatientPriceItemClickListener {

    fun onItemClicked(price : Price)
}