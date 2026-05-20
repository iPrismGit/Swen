package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.DoctorItemBinding
import com.iprism.swen.databinding.ItemLoadingBinding
import com.iprism.swen.interfaces.OnDoctorItemClickListener
import com.iprism.swen.models.onlinedoctors.DoctorsItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.DoctorItemViewHolder
import com.iprism.swen.viewholders.ItemLoadingViewHolder

class DoctorsAdapter(private val doctors : ArrayList<DoctorsItem?>, private var isHospitalVisit : Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onDoctorItemClickListener: OnDoctorItemClickListener? = null

    fun setOnDoctorItemClickListener(onDoctorItemClickListener: OnDoctorItemClickListener?) {
        this.onDoctorItemClickListener = onDoctorItemClickListener
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (doctors[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DoctorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ITEM) {
            DoctorItemViewHolder(binding)
        } else {
            ItemLoadingViewHolder(itemLoadingBinding)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DoctorItemViewHolder) {
            val doctor = doctors[position]!!
            val context = holder.binding.root.context
            holder.binding.nameTxt.text = doctor.name
            holder.binding.specialityTxt.text = doctor.specialization
            if (doctor.image.isNotEmpty()) {
                Glide.with(context)
                    .load(Constants.IMAGES_BASE_URL + doctor.image)
                    .into(holder.binding.doctorImg)
            }
            holder.binding.studyTxt.text = doctor.qualification
            holder.binding.ratingBar.rating = doctor.rating.toFloat()
            holder.binding.consultationsCountTxt.text =
                "${doctor.consultations} ${if (doctor.consultations.toInt() > 1) context.getString(R.string.consultations) else context.getString(R.string.consultation)}"
            holder.binding.expTxt.text =
                "${doctor.exp} ${if (doctor.exp.toInt() > 1) context.getString(R.string.years) else context.getString(R.string.year)}"
            if (isHospitalVisit) {
                holder.binding.hospitalVisitBtn.visibility = View.VISIBLE
            }
            holder.binding.root.setOnClickListener(View.OnClickListener {
                onDoctorItemClickListener!!.onItemClicked(doctor)
            })
            holder.binding.onlineConsultBtn.setOnClickListener(View.OnClickListener {
                onDoctorItemClickListener!!.onItemClicked(doctor)
            })
            holder.binding.hospitalVisitBtn.setOnClickListener(View.OnClickListener {
                onDoctorItemClickListener!!.onItemClicked(doctor)
            })
        }
    }

    override fun getItemCount(): Int {
        return doctors.size
    }

    fun showLoadingFooter() {
        doctors.add(null)
        notifyItemInserted(doctors.size - 1)
    }

    fun removeLoadingFooter() {
        val index = doctors.indexOf(null)
        if (index != -1) {
            doctors.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
