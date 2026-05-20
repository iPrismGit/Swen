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
import com.iprism.swen.interfaces.OnHospitalDoctorItemClickListener
import com.iprism.swen.models.hospitaldoctors.DoctorsItem
import com.iprism.swen.utils.Constants
import com.iprism.swen.viewholders.DoctorItemViewHolder
import com.iprism.swen.viewholders.ItemLoadingViewHolder

class HospitalDoctorsAdapter(private val doctors : ArrayList<DoctorsItem?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onHospitalDoctorItemClickListener: OnHospitalDoctorItemClickListener? = null

    fun setOnDoctorItemClickListener(onHospitalDoctorItemClickListener: OnHospitalDoctorItemClickListener?) {
        this.onHospitalDoctorItemClickListener = onHospitalDoctorItemClickListener
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
            val context = holder.binding.root.context
            val doctor = doctors[position]!!
            holder.binding.nameTxt.text = doctor.name
            holder.binding.specialityTxt.text = doctor.specialization
            holder.binding.hospitalNameTxt.visibility = View.VISIBLE
            holder.binding.hospitalNameTxt.text = doctor.hospitalName
            if (doctor.image.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(Constants.IMAGES_BASE_URL + doctor.image)
                    .into(holder.binding.doctorImg)
            }
            holder.binding.studyTxt.text = doctor.qualification
            holder.binding.ratingBar.rating = doctor.rating.toFloat()
            holder.binding.consultationsCountTxt.text =
                "${doctor.consultations} ${if (doctor.consultations.toInt() > 1) context.getString(R.string.consultations) else context.getString(R.string.consultation)}"
            holder.binding.expTxt.text =
                "${doctor.exp} ${if (doctor.exp.toInt() > 1) context.getString(R.string.years) else context.getString(R.string.year)}"
            if (doctor.consultType.equals("offline", true)) {
                holder.binding.hospitalVisitBtn.visibility = View.VISIBLE
                holder.binding.onlineConsultBtn.visibility = View.GONE
            } else {
                holder.binding.hospitalVisitBtn.visibility = View.VISIBLE
                holder.binding.onlineConsultBtn.visibility = View.VISIBLE
            }
            holder.binding.root.setOnClickListener(View.OnClickListener {
                onHospitalDoctorItemClickListener!!.onItemClicked(doctor)
            })
            holder.binding.onlineConsultBtn.setOnClickListener(View.OnClickListener {
                onHospitalDoctorItemClickListener!!.onItemClicked(doctor)
            })
            holder.binding.hospitalVisitBtn.setOnClickListener(View.OnClickListener {
                onHospitalDoctorItemClickListener!!.onItemClicked(doctor)
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
