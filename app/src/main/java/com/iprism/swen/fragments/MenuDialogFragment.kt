package com.iprism.swen.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.iprism.swen.R
import com.iprism.swen.activities.AboutUsActivity
import com.iprism.swen.activities.AddressListActivity
import com.iprism.swen.activities.ContactUsActivity
import com.iprism.swen.activities.DiagnosticTestsBookingsActivity
import com.iprism.swen.activities.ECardActivity
import com.iprism.swen.activities.EditProfileActivity
import com.iprism.swen.activities.HospitalAdmissionBookingsActivity
import com.iprism.swen.activities.HospitalAmbulanceBookingsActivity
import com.iprism.swen.activities.HospitalDiagnosticTestsBookingsActivity
import com.iprism.swen.activities.HospitalDoctorsBookingsActivity
import com.iprism.swen.activities.HospitalMedicineBookingsActivity
import com.iprism.swen.activities.LabTestsBookingsActivity
import com.iprism.swen.activities.LanguagesActivity
import com.iprism.swen.activities.LoginActivity
import com.iprism.swen.activities.MedLockerActivity
import com.iprism.swen.activities.OffersActivity
import com.iprism.swen.activities.OnlineDoctorsBookingsActivity
import com.iprism.swen.activities.PharmacyBookingsActivity
import com.iprism.swen.activities.ShareAppActivity
import com.iprism.swen.activities.SubscriptionActivity
import com.iprism.swen.activities.SubscriptionDetailsActivity
import com.iprism.swen.databinding.FragmentMenuDialogBinding
import com.iprism.swen.databinding.LogOutDialogBinding
import com.iprism.swen.utils.User

class MenuDialogFragment : DialogFragment() {

    private var binding: FragmentMenuDialogBinding? = null
    private var isSubscribe: Boolean? = null

    companion object {
        private const val ARG_SUB_STATUS = "arg_sub_status"

        fun newInstance(subStatus: Boolean): MenuDialogFragment {
            return MenuDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SUB_STATUS, subStatus)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get argument
        isSubscribe = arguments?.getBoolean(ARG_SUB_STATUS)
        Log.d("substatus", isSubscribe.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMenuDialogBinding.inflate(layoutInflater)
        handleCrossImg()
        handleYourAddress()
        handleContactUs()
        handleAboutUs()
        handleLogout()
        handleOffersLl()
        handleProfileLL()
        handleMedLocker()
        handleDiagnostic()
        handleLabTests()
        handleSubscription()
        handleShareApp()
        handleWellness()
        handleOnline()
        handleUpArrowImg()
        handleHospitalMedicineBookingsLL()
        handleHospitalAdmissionBookingsLL()
        handleHospitalDiagnosticBookingsLL()
        handleHospitalDoctorBookingsLL()
        handleHospitalAmbulanceBookingsLL()
        handleLanguageImg()
        handleDownArrowImg()
        handleECard()
        handleAckoInsurance()
        return binding!!.root
    }

    private fun handleECard() {
        binding!!.eCardLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                startActivity(Intent(requireContext(), ECardActivity::class.java))
            }
        })
    }

    private fun handleAckoInsurance() {
        binding!!.ackoInsuranceLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                val url = "https://www.acko.com/myaccount"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val marginInPx = (16 * resources.displayMetrics.density).toInt() // Convert 16dp to pixels
            setLayout(
                resources.displayMetrics.widthPixels - (marginInPx * 2),  // Full width minus margins
                resources.displayMetrics.heightPixels - (marginInPx * 2) // Full height minus margins
            )
            setBackgroundDrawableResource(android.R.color.transparent) // Optional for rounded corners
        }
    }

    private fun handleCrossImg() {
        binding!!.cancelImg.setOnClickListener(View.OnClickListener {
            dialog!!.cancel()
        })
    }

    private fun handleContactUs() {
        binding!!.contactUsLl.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), ContactUsActivity::class.java))
        })
    }

    private fun handleLanguageImg() {
        binding!!.languageImg.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireContext(), LanguagesActivity::class.java)
            intent.putExtra("tag", "fromProfile")
            startActivity(intent)
            dialog!!.cancel()
        })
    }

    private fun handleUpArrowImg() {
        binding!!.upArrowImg.setOnClickListener(View.OnClickListener {
            binding!!.hospitalBookingsDetailsLl.visibility = View.GONE
            binding!!.upArrowImg.visibility = View.GONE
            binding!!.downArrowImg.visibility = View.VISIBLE
        })
    }

    private fun handleDownArrowImg() {
        binding!!.downArrowImg.setOnClickListener(View.OnClickListener {
            binding!!.hospitalBookingsDetailsLl.visibility = View.VISIBLE
            binding!!.upArrowImg.visibility = View.VISIBLE
            binding!!.downArrowImg.visibility = View.GONE
        })
    }

    private fun handleHospitalMedicineBookingsLL() {
        binding!!.hospitalMedicineBookingsLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                startActivity(
                    Intent(
                        requireContext(),
                        HospitalMedicineBookingsActivity::class.java
                    )
                )
            }
        })
    }

    private fun handleHospitalAdmissionBookingsLL() {
        binding!!.hospitalAdmissionBookingsLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                startActivity(
                    Intent(
                        requireContext(),
                        HospitalAdmissionBookingsActivity::class.java
                    )
                )
            }
        })
    }

    private fun handleHospitalDoctorBookingsLL() {
        binding!!.hospitalDoctorBookingsLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                startActivity(Intent(requireContext(), HospitalDoctorsBookingsActivity::class.java))
            }
        })
    }

    private fun handleHospitalDiagnosticBookingsLL() {
        binding!!.hospitalDiagnosticBookingsLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                startActivity(
                    Intent(
                        requireContext(),
                        HospitalDiagnosticTestsBookingsActivity::class.java
                    )
                )
            }
        })
    }

    private fun handleHospitalAmbulanceBookingsLL() {
        binding!!.hospitalAmbulanceBookingsLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                startActivity(
                    Intent(
                        requireContext(),
                        HospitalAmbulanceBookingsActivity::class.java
                    )
                )
            }
        })
    }

    private fun handleAboutUs() {
        binding!!.aboutUsLl.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), AboutUsActivity::class.java))
        })
    }

    private fun handleYourAddress() {
        binding!!.yourAddressLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                startActivity(Intent(requireContext(), AddressListActivity::class.java))
            }
        })
    }

    private fun handleOffersLl() {
        binding!!.offersLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                startActivity(Intent(requireContext(), OffersActivity::class.java))
            }
        })
    }

    private fun handleLogout() {
        binding!!.logoutLl.setOnClickListener(View.OnClickListener {
            showLogOutDialog()
        })
    }

    private fun handleProfileLL() {
        binding!!.profileLl.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        })
    }

    private fun handleLabTests() {
        binding!!.labTestLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                val intent = Intent(requireContext(), LabTestsBookingsActivity::class.java)
                intent.putExtra("tag", "labTest")
                startActivity(intent)
            }
        })
    }

    private fun handleWellness() {
        binding!!.wellnessLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                val intent = Intent(requireContext(), PharmacyBookingsActivity::class.java)
                intent.putExtra("tag", "wellness")
                startActivity(intent)
            }
        })
    }

    private fun handleOnline() {
        binding!!.onlineDoctorsLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                val intent = Intent(requireContext(), OnlineDoctorsBookingsActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun handleDiagnostic() {
        binding!!.diagnosticLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                val intent = Intent(requireContext(), DiagnosticTestsBookingsActivity::class.java)
                intent.putExtra("tag", "diagnostic")
                startActivity(intent)
            }
        })
    }

    private fun handleMedLocker() {
        binding!!.medlockerLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                startActivity(Intent(requireContext(), MedLockerActivity::class.java))
            }
        })
    }

    private fun handleSubscription() {
        binding!!.subscriptionLl.setOnClickListener(View.OnClickListener {
            handleRestrictedClick {
                startActivity(Intent(requireContext(), SubscriptionDetailsActivity::class.java))
            }
        })
    }

    private fun handleShareApp() {
        binding!!.shareappLl.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), ShareAppActivity::class.java))
        })
    }

    private fun showLogOutDialog() {
        val dialog = Dialog(requireContext())
        val logoutBinding = LogOutDialogBinding.inflate(layoutInflater)
        dialog.setContentView(logoutBinding.root)
        dialog.window?.setBackgroundDrawableResource(R.drawable.edit_text_bg)
        logoutBinding.yesBtn.setOnClickListener(View.OnClickListener {
            val user = User(requireContext())
            user.logoutUser()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
            requireActivity().finish()
        })

        logoutBinding.noBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        dialog.show()
    }

    private fun handleRestrictedClick(action: () -> Unit) {
       /* if (isSubscribe == true) {
            action()
        } else {
            val intent = Intent(requireContext(), SubscriptionActivity::class.java)
            intent.putExtra("tag", "subscribe")
            startActivity(intent)
            Toast.makeText(requireContext(), "Please subscribe to use this feature", Toast.LENGTH_SHORT).show()
        }*/
        action()
    }
}