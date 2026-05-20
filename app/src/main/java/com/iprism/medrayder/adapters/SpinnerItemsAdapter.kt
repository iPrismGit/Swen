package com.iprism.medrayder.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.iprism.medrayder.databinding.SpinnerItemBinding
import com.iprism.medrayder.models.userdropdowns.CategoriesItem

class SpinnerItemsAdapter(context: Context, resource: Int, private val items: List<CategoriesItem>) : ArrayAdapter<CategoriesItem>(context, resource, items) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val item = getItem(position)
        binding.text1.text = item!!.name
        return binding.root
    }

    override fun getItem(position: Int): CategoriesItem? {
        return super.getItem(position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
