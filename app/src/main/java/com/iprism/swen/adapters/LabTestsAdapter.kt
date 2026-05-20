package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.HealthPackageItemBinding
import com.iprism.swen.databinding.ItemLoadingBinding
import com.iprism.swen.interfaces.OnDiagnosticTestItemClickListener
import com.iprism.swen.models.labtests.MainDataItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.HealthPackageViewHolder
import com.iprism.swen.viewholders.ItemLoadingViewHolder

class LabTestsAdapter(private var labTests: ArrayList<MainDataItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onDiagnosticTestItemClickListener: OnDiagnosticTestItemClickListener? = null

    fun setOnDoctorItemClickListener(onDiagnosticTestItemClickListener: OnDiagnosticTestItemClickListener?) {
        this.onDiagnosticTestItemClickListener = onDiagnosticTestItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (labTests[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
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
            val test = labTests[position]
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
        return labTests.size
    }

    fun showLoadingFooter() {
        labTests.add(null)
        notifyItemInserted(labTests.size - 1)
    }

    fun removeLoadingFooter() {
        val index = labTests.indexOf(null)
        if (index != -1) {
            labTests.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
