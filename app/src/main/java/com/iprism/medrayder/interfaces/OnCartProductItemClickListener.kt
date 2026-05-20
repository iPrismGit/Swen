package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.pharmacyproductcart.ProductsItem
import com.iprism.medrayder.models.pharmacyproducts.MainDataItem


interface OnCartProductItemClickListener {

    fun onIncrementClicked(product : ProductsItem)

    fun onDecrementClicked(product : ProductsItem)
}