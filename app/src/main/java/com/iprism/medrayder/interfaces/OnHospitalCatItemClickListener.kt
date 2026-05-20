package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.homepage.CategoriesItem
import com.iprism.medrayder.models.homepage.SubCategoriesItem

interface OnHospitalCatItemClickListener {

    fun onItemClicked(item : SubCategoriesItem)
}