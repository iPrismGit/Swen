package com.iprism.medrayder.interfaces

import android.widget.GridLayout.Spec
import com.iprism.medrayder.models.filters.SpecialitiesItem


interface OnFilterSpecialityItemActionListener {

    fun onFilterValueClicked(position : Int, item : SpecialitiesItem)
}