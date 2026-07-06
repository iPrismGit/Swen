package com.iprism.swen.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.ecmuser.models.HospitalAssistanceApiRequest
import com.iprism.swen.databinding.ActivityWhatsappAssistanceBinding
import com.iprism.swen.repository.CommonRepository
import com.iprism.swen.utils.UiState
import com.iprism.swen.utils.User
import com.iprism.swen.utils.showToast
import com.iprism.swen.viewmodels.HomeViewModel
import com.iprism.swen.viewmodels.ViewModelFactory

class WhatsappAssistanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhatsappAssistanceBinding
    private lateinit var viewModel: HomeViewModel
    private var mobileNumber = ""

    private lateinit var user : User
    private lateinit var  userDetails : HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        user = User(this)
        userDetails = user.getUserDetails()
        binding = ActivityWhatsappAssistanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        handleGetStartedBtn()
        initViewModel()
        observeWhatsappAssistanceResponse()
        val hospitalAssistanceApiRequest = HospitalAssistanceApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            "1",
            userDetails[User.ID].toString()
        )
        NetworkRetryHelper.checkAndCallWithRetry(this, hospitalAssistanceApiRequest) { req ->
            viewModel.fetchWhatsappAssistanceResponse(req)
        }
    }

    private fun initViewModel() {
        val repository = CommonRepository()
        val factory = ViewModelFactory { HomeViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun observeWhatsappAssistanceResponse() {
        viewModel.whatsappAssistanceResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.shimmerLo.visibility = View.VISIBLE
                    binding.mainLo.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.shimmerLo.visibility = View.GONE
                    binding.mainLo.visibility = View.VISIBLE
                    mobileNumber = result.data.mobile
                }

                is UiState.Error -> {
                    showToast(result.message)
                    binding.shimmerLo.visibility = View.VISIBLE
                    binding.mainLo.visibility = View.GONE
                }
            }
        }
    }

    private fun handleGetStartedBtn() {
        binding.getStartedBtn.setOnClickListener { view ->
            openWhatsAppWithoutMessage(this)
        }
    }

    fun openWhatsAppWithoutMessage(context: Context) {
        try {
            val url = "https://api.whatsapp.com/send?phone=+91$mobileNumber"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to open WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

}