package com.iprism.medrayder.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.HealthPackageItemBinding
import com.iprism.medrayder.databinding.TestsBottomSheetBinding
import com.iprism.medrayder.viewholders.HealthPackageViewHolder

class HealthPackagesAdapter(var images: Int) : RecyclerView.Adapter<HealthPackageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthPackageViewHolder {
        val binding = HealthPackageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HealthPackageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HealthPackageViewHolder, position: Int) {
        holder.binding.numberOfTestsTxt.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        holder.binding.previousPriceTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.numberOfTestsTxt.setOnClickListener(View.OnClickListener {
            showTestsSheet(holder.binding.root.context)
        })
        holder.binding.bookBtn.setOnClickListener(View.OnClickListener {
            holder.binding.bookBtn.visibility = View.GONE
            holder.binding.addedBtn.visibility = View.VISIBLE
        })
    }

    override fun getItemCount(): Int = 8

    private fun showTestsSheet(context: Context) {
        var bottomSheetDialog = BottomSheetDialog(context)
        val termsBinding = TestsBottomSheetBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(termsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog -> val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.top_edges_bg)
        }
        bottomSheetDialog.show()
    }
}
