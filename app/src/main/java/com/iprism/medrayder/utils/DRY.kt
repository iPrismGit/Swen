package com.iprism.medrayder.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.iprism.medrayder.R

object DRY {

    fun showMaps(context : Context, lat : String, lon : String) {
        val url = "https://www.google.com/maps/search/?api=1&query=$lat,$lon"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    fun updateOrderStatus(
        context: Context,
        status: String,
        stepPlaced: View,
        stepProcessed: View,
        stepCompleted: View,
        labelPlaced: TextView,
        labelProcessed: TextView,
        labelCompleted: TextView,
        progressLine1: View,
        progressLine2: View
    ) {
        when (status.lowercase()) {
            "1" -> {
                stepPlaced.setBackgroundResource(R.drawable.blue_img)
                stepProcessed.setBackgroundResource(R.drawable.grey_img)
                stepCompleted.setBackgroundResource(R.drawable.grey_img)

                progressLine1.setBackgroundResource(R.color.gray5)
                progressLine2.setBackgroundResource(R.color.gray5)

                labelPlaced.setTextColor(ContextCompat.getColor(context, R.color.blue))
                labelProcessed.setTextColor(ContextCompat.getColor(context, R.color.black))
                labelCompleted.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            "2" -> {
                stepPlaced.setBackgroundResource(R.drawable.blue_img)
                stepProcessed.setBackgroundResource(R.drawable.blue_img)
                stepCompleted.setBackgroundResource(R.drawable.grey_img)

                progressLine1.setBackgroundResource(R.color.blue)
                progressLine2.setBackgroundResource(R.color.gray5)

                labelPlaced.setTextColor(ContextCompat.getColor(context, R.color.blue))
                labelProcessed.setTextColor(ContextCompat.getColor(context, R.color.blue))
                labelCompleted.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            "3" -> {
                stepPlaced.setBackgroundResource(R.drawable.blue_img)
                stepProcessed.setBackgroundResource(R.drawable.blue_img)
                stepCompleted.setBackgroundResource(R.drawable.blue_img)

                progressLine1.setBackgroundResource(R.color.blue)
                progressLine2.setBackgroundResource(R.color.blue)

                labelPlaced.setTextColor(ContextCompat.getColor(context, R.color.blue))
                labelProcessed.setTextColor(ContextCompat.getColor(context, R.color.blue))
                labelCompleted.setTextColor(ContextCompat.getColor(context, R.color.blue))
            }
        }
    }

    fun updateTwoStepOrderStatus(
        context: Context,
        status: String,
        stepPlaced: View,
        stepDelivered: View,
        labelPlaced: TextView,
        labelDelivered: TextView,
        progressLine: View
    ) {
        when (status.lowercase()) {
            "1" -> {
                stepPlaced.setBackgroundResource(R.drawable.blue_img)
                stepDelivered.setBackgroundResource(R.drawable.grey_img)

                progressLine.setBackgroundResource(R.color.gray5)

                labelPlaced.setTextColor(ContextCompat.getColor(context, R.color.blue))
                labelDelivered.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            "2" -> {
                stepPlaced.setBackgroundResource(R.drawable.blue_img)
                stepDelivered.setBackgroundResource(R.drawable.blue_img)

                progressLine.setBackgroundResource(R.color.blue)

                labelPlaced.setTextColor(ContextCompat.getColor(context, R.color.blue))
                labelDelivered.setTextColor(ContextCompat.getColor(context, R.color.blue))
            }
        }
    }
}