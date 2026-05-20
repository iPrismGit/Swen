package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.treatmentplaning.TreatmentPlanningItem

interface OnTreatmentPlaningItemClickListener {

    fun onItemClicked(treatmentPlanningItem : TreatmentPlanningItem)
}