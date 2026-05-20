package com.iprism.swen.interfaces

import com.iprism.swen.models.onlinedoctorscoupons.CouponsItem

interface OnCouponItemClickListener {

    fun onItemClicked(coupon : CouponsItem)
}