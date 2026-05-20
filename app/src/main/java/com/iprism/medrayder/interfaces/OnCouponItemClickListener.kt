package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.onlinedoctorscoupons.CouponsItem

interface OnCouponItemClickListener {

    fun onItemClicked(coupon : CouponsItem)
}