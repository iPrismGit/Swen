package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.medrayder.databinding.PdfItemBinding
import com.iprism.medrayder.interfaces.PdfItemClickListener
import com.iprism.medrayder.models.labtestbookingdetails.ReportsItem
import com.iprism.medrayder.viewholders.PdfItemViewHolder

class PdfsAdapter(private val pdfs : List<ReportsItem>) : RecyclerView.Adapter<PdfItemViewHolder>() {

    private var pdfItemClickListener: PdfItemClickListener? = null

    fun setOnPdfItemClickListener(pdfItemClickListener: PdfItemClickListener?) {
        this.pdfItemClickListener = pdfItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfItemViewHolder {
        val binding = PdfItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PdfItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PdfItemViewHolder, position: Int) {
        val pdf = pdfs[position]
        holder.binding.pdfNameTxt.text = pdf.image
        holder.binding.root.setOnClickListener(View.OnClickListener {
            pdfItemClickListener!!.onItemClicked(pdf.image)
        })
    }

    override fun getItemCount(): Int {
        return pdfs.size
    }
}
