package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.medrayder.R
import com.iprism.medrayder.databinding.HealthPackageItemBinding
import com.iprism.medrayder.databinding.ItemLoadingBinding
import com.iprism.medrayder.databinding.TestsBottomSheetBinding
import com.iprism.medrayder.interfaces.OnDiagnosticTestItemClickListener
import com.iprism.medrayder.models.diagnostictests.MainDataItem
import com.iprism.medrayder.utils.Constants
import com.iprism.medrayder.viewholders.HealthPackageViewHolder
import com.iprism.medrayder.viewholders.ItemLoadingViewHolder

class DiagnosticTestsAdapter(private var diagnosticTests: ArrayList<MainDataItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onDiagnosticTestItemClickListener: OnDiagnosticTestItemClickListener? = null

    fun setOnDoctorItemClickListener(onDiagnosticTestItemClickListener: OnDiagnosticTestItemClickListener?) {
        this.onDiagnosticTestItemClickListener = onDiagnosticTestItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (diagnosticTests[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = HealthPackageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            HealthPackageViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HealthPackageViewHolder) {
            val context = holder.binding.root.context
            val test = diagnosticTests[position]
            if (test!!.image.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(Constants.IMAGES_BASE_URL + test.image)
                    .into(holder.binding.testImg)
            }
            holder.binding.testNameTxt.text = test.name.replaceFirstChar { it.uppercase() }
            holder.binding.numberOfTestsTxt.text = "${context.getString(R.string.contains)} ${test.tests.size} ${context.getString(R.string.tests)}"
            holder.binding.priceTxt.text = "₹ ${test.onePersonDiscount}"
            holder.binding.previousPriceTxt.text = "₹ ${test.onePerson}"
            holder.binding.reportsTxt.text = "${context.getString(R.string.reports_in)} ${test.reportIn} ${context.getString(R.string.hrs)}"
            if (test.fasting.equals("yes", true)) {
                holder.binding.fastingTxt.text = context.getString(R.string.fasting_required)
            } else {
                holder.binding.fastingTxt.text = context.getString(R.string.fasting_not_required)
            }
            holder.binding.numberOfTestsTxt.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            holder.binding.previousPriceTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.numberOfTestsTxt.setOnClickListener(View.OnClickListener {
                onDiagnosticTestItemClickListener!!.onTestsClicked(test.tests)
            })
            holder.binding.bookBtn.setOnClickListener(View.OnClickListener {
                onDiagnosticTestItemClickListener!!.onBookClicked(test.id.toString())
            })
        }
    }

    override fun getItemCount(): Int {
        return diagnosticTests.size
    }

    fun showLoadingFooter() {
        diagnosticTests.add(null)
        notifyItemInserted(diagnosticTests.size - 1)
    }

    fun removeLoadingFooter() {
        val index = diagnosticTests.indexOf(null)
        if (index != -1) {
            diagnosticTests.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
