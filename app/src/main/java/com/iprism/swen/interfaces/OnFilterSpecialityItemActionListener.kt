package com.iprism.swen.interfaces

import com.iprism.swen.models.filters.SpecialitiesItem


interface OnFilterSpecialityItemActionListener {

    fun onFilterValueClicked(position : Int, item : SpecialitiesItem)
}