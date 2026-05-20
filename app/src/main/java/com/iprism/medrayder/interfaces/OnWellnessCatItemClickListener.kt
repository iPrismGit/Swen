package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.homepage.CategoriesItem
import com.iprism.medrayder.models.homepage.PharmacyCategoriesItem

interface OnWellnessCatItemClickListener {

    fun onItemClicked(item : PharmacyCategoriesItem)
}