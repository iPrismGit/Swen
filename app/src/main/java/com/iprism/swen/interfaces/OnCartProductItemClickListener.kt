package com.iprism.swen.interfaces

import com.iprism.swen.models.pharmacyproductcart.ProductsItem


interface OnCartProductItemClickListener {

    fun onIncrementClicked(product : ProductsItem)

    fun onDecrementClicked(product : ProductsItem)
}