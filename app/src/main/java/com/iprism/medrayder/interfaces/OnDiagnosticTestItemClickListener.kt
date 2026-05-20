package com.iprism.medrayder.interfaces

import com.iprism.medrayder.models.diagnostictests.TestsItem

interface OnDiagnosticTestItemClickListener {

    fun onTestsClicked(tests : List<TestsItem>)
    fun onBookClicked(testId : String)
}