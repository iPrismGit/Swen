import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.iprism.medrayder.R
import com.iprism.medrayder.activities.LoginActivity
import com.iprism.medrayder.databinding.LogOutDialogBinding
import com.iprism.medrayder.databinding.NoInternetDialogBinding
import com.iprism.medrayder.utils.User

object NetworkRetryHelper {
    fun <T> checkAndCallWithRetry(
        context: Context,
        request: T,
        apiCall: (T) -> Unit
    ) {
        if (context.isConnected()) {
            apiCall(request)
        } else {
            context.showNoInternetDialog {
                checkAndCallWithRetry(context, request, apiCall)
            }
        }
    }

    private fun Context.showNoInternetDialog(onRetry: () -> Unit) {
        val dialog = Dialog(this, android.R.style.ThemeOverlay)
        val binding = NoInternetDialogBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)

        binding.refreshBtn.setOnClickListener {
            dialog.dismiss()
            onRetry()
        }

        dialog.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        dialog.show()
    }

    private fun Context.isConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
