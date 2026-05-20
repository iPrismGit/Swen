package com.iprism.swen.interfaces

import com.iprism.swen.models.subscription.HealthCardsItem

interface OnSubscriptionItemClickListener {

    fun onItemClicked(plan : HealthCardsItem)
}