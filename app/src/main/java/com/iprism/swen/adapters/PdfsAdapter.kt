package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.swen.databinding.PdfItemBinding
import com.iprism.swen.interfaces.PdfItemClickListener
import com.iprism.swen.models.labtestbookingdetails.ReportsItem
import com.iprism.swen.viewholders.PdfItemViewHolder

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
