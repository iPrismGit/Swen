package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.homepage.CategoriesItem
import com.iprism.medrayder.models.homepage.SubCategoriesItem

interface OnLabCatItemClickListener {

    fun onItemClicked(item : CategoriesItem)
}