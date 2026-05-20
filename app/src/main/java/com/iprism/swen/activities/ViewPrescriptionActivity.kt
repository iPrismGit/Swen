package com.iprism.swen.activities

import android.annotation.SuppressLint
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.swen.R
import com.iprism.swen.adapters.PreviewMedicinesAdapter
import com.iprism.swen.adapters.PreviewNotesAdapter
import com.iprism.swen.adapters.PreviewTestsAdapter
import com.iprism.swen.databinding.ActivityViewPrescriptionBinding
import com.iprism.swen.models.onlinedoctorsinglebookingdetails.History
import com.iprism.swen.utils.showToast
import java.io.File
import java.io.FileOutputStream

class ViewPrescriptionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityViewPrescriptionBinding
    private var bookingDetails : History? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        if (intent.hasExtra("details")) {
            bookingDetails = intent.getSerializableExtra("details") as History
            if (bookingDetails!!.medicines.isNotEmpty()) {
                setupMedicines()
            } else {
                binding.medicinesLl.visibility = View.GONE
            }
            if (bookingDetails!!.tests.isNotEmpty()) {
                setupTests()
            } else {
                binding.testsLl.visibility = View.GONE
            }
            if (bookingDetails!!.notes.isNotEmpty()) {
                setupNotes()
            } else {
                binding.notesLl.visibility = View.GONE
            }
            binding.doctorNameTxt.text = bookingDetails!!.name
            binding.doctorDegreesTxt.text = bookingDetails!!.qualification
            binding.dateTxt.text = bookingDetails!!.date
            binding.patientNameTxt.text = "${getString(R.string.patient_name)} : ${bookingDetails!!.patientName}"
            binding.patientDobTxt.text = "${getString(R.string.date_of_birth1)} : ${bookingDetails!!.dob}"
            binding.patientGenderTxt.text = "${getString(R.string.gender1)} : ${bookingDetails!!.gender.replaceFirstChar { it.uppercase() }}"
            binding.patientBloodGroupTxt.text = "${getString(R.string.blood_group1)} : ${bookingDetails!!.bloodGroup}"
        }
        handleDownloadImg()
    }

    private fun setupMedicines() {
        val previewMedicinesAdapter = PreviewMedicinesAdapter(bookingDetails!!.medicines)
        binding.medicinesRv.adapter = previewMedicinesAdapter
        binding.medicinesRv.layoutManager = LinearLayoutManager(this)
    }

    private fun setupTests() {
        val previewTestsAdapter = PreviewTestsAdapter(bookingDetails!!.tests)
        binding.testsRv.adapter = previewTestsAdapter
        binding.testsRv.layoutManager = LinearLayoutManager(this)
    }

    private fun setupNotes() {
        val notesAdapter = PreviewNotesAdapter(bookingDetails!!.notes)
        binding.notesRv.adapter = notesAdapter
        binding.notesRv.layoutManager = LinearLayoutManager(this)
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleDownloadImg() {
        binding.downloadImg.setOnClickListener(View.OnClickListener {
            createPdfFromView(binding.previewLl, "Prescription" + "(" + bookingDetails!!.bookingId + ")")
        })
    }

    private fun createPdfFromView(view: View, fileName: String) {
        val document = PdfDocument()

        // Measure and layout the view
        view.measure(
            View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val pageInfo = PdfDocument.PageInfo.Builder(view.width, view.height, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        view.draw(canvas)
        document.finishPage(page)

        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsPath, "$fileName.pdf")
        val uniqueFile = getUniqueFile(file)

        try {
            document.writeTo(FileOutputStream(uniqueFile))
            showToast("${getString(R.string.pdf_saved_to)}: ${uniqueFile.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("${getString(R.string.error_saving_pdf)}: ${e.message}")
        }

        document.close()
    }

    fun getUniqueFile(file: File): File {
        var newFile = file
        var fileName = file.nameWithoutExtension
        val extension = file.extension
        var counter = 1

        while (newFile.exists()) {
            val newName = "$fileName($counter).$extension"
            newFile = File(file.parent, newName)
            counter++
        }
        return newFile
    }
}