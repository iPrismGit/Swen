package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.addresslist.AddressItem
import com.iprism.medrayder.models.subscription.HealthCardsItem

interface OnSubscriptionItemClickListener {

    fun onItemClicked(plan : HealthCardsItem)
}