package com.iprism.swen.interfaces

import com.iprism.swen.models.diagnostictests.TestsItem

interface OnDiagnosticTestItemClickListener {

    fun onTestsClicked(tests : List<TestsItem>)
    fun onBookClicked(testId : String)
}