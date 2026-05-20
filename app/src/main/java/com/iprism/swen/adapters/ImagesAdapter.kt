package com.iprism.swen.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.swen.R
import com.iprism.swen.databinding.ImageItemBinding
import com.iprism.swen.interfaces.OnImageDeleteActionListener
import com.iprism.swen.viewholders.ImageViewHolder

class ImagesAdapter : RecyclerView.Adapter<ImageViewHolder>() {

    private var checkInImages: List<Uri>? = null
    private var onImageDeleteActionListener: OnImageDeleteActionListener? = null

    fun setCheckInImages(checkInImages: List<Uri>?) {
        this.checkInImages = checkInImages
        notifyDataSetChanged()
    }

    fun setOnDeleteActionListener(onImageDeleteActionListener: OnImageDeleteActionListener?) {
        this.onImageDeleteActionListener = onImageDeleteActionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val taskImage = checkInImages!![position]
        val context = holder.itemView.context
        val mimeType = context.contentResolver.getType(taskImage)
        if (mimeType == "application/pdf") {
            // It's a PDF - show toast and hide image
            holder.binding.checkInImg.setImageResource(R.drawable.pdf_img1)
            holder.binding.checkInImg.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
            val cursor = context.contentResolver.query(taskImage, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    Log.d("PDF_NAME", "Selected file: $displayName")
                    holder.binding.pdfNameTxt.visibility = View.VISIBLE
                    holder.binding.pdfNameTxt.text = displayName
                }
            }
        } else {
            Glide.with(holder.itemView)
                .load(taskImage)
                .into(holder.binding.checkInImg)
            holder.binding.pdfNameTxt.visibility = View.GONE
        }
        holder.binding.deleteImg.setOnClickListener { view: View? ->
            onImageDeleteActionListener!!.onDelete(position)
        }
    }

    override fun getItemCount(): Int {
        return checkInImages!!.size
    }
}