package com.iprism.swen.interfaces

import com.iprism.swen.models.pharmacyproducts.MainDataItem


interface OnProductItemClickListener {

    fun onAddClicked(product : MainDataItem, position : Int)

    fun onIncrementClicked(product : MainDataItem, position : Int)

    fun onDecrementClicked(product : MainDataItem, position : Int)
}