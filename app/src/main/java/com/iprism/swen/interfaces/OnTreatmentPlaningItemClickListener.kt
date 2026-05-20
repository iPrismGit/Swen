package com.iprism.swen.interfaces

import com.iprism.swen.models.treatmentplaning.TreatmentPlanningItem

interface OnTreatmentPlaningItemClickListener {

    fun onItemClicked(treatmentPlanningItem : TreatmentPlanningItem)
}