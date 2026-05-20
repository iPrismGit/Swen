package com.iprism.swen.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iprism.swen.R
import com.iprism.swen.activities.AddFamilyMemberActivity
import com.iprism.swen.activities.SubscriptionActivity
import com.iprism.swen.databinding.AddFamilyMembersBsBinding


class AddFamilyMembersBs : BottomSheetDialogFragment() {

    private lateinit var binding: AddFamilyMembersBsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddFamilyMembersBsBinding.inflate(layoutInflater)
        handleContinueBtn()
        handleCrossImg()
        isCancelable = false
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private fun handleContinueBtn() {
        binding.addFamilyBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireContext(), AddFamilyMemberActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleCrossImg() {
        binding.crossImg.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), SubscriptionActivity::class.java))
        })
    }
}