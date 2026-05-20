package com.iprism.medrayder.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iprism.medrayder.R
import com.iprism.medrayder.activities.AddFamilyMemberActivity
import com.iprism.medrayder.activities.HomeActivity
import com.iprism.medrayder.activities.SubscriptionActivity
import com.iprism.medrayder.adapters.AddressListAdapter
import com.iprism.medrayder.databinding.AddFamilyMembersBsBinding
import com.iprism.medrayder.databinding.FragmentLocationBottomSheetBinding
import com.iprism.medrayder.interfaces.OnAddressItemClickListener
import com.iprism.medrayder.models.addresslist.AddressItem
import com.iprism.medrayder.models.addresslist.AddressListRequest
import com.iprism.medrayder.repository.AddressRepository
import com.iprism.medrayder.utils.UiState
import com.iprism.medrayder.utils.User
import com.iprism.medrayder.utils.getUserDetails
import com.iprism.medrayder.utils.showToast
import com.iprism.medrayder.viewmodels.AddressListViewModel
import com.iprism.medrayder.viewmodels.ViewModelFactory


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