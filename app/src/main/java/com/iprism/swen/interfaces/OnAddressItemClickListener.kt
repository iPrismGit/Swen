package com.iprism.swen.interfaces

import com.iprism.swen.models.addresslist.AddressItem

interface OnAddressItemClickListener {

    fun onItemClicked(address : AddressItem)
}