package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.pharmacyproducts.MainDataItem


interface OnProductItemClickListener {

    fun onAddClicked(product : MainDataItem, position : Int)

    fun onIncrementClicked(product : MainDataItem, position : Int)

    fun onDecrementClicked(product : MainDataItem, position : Int)
}