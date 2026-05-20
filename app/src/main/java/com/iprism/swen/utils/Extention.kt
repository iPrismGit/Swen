package com.iprism.swen.utils

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.wang.avi.AVLoadingIndicatorView

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun AVLoadingIndicatorView.showProgress() {
    this.visibility = View.VISIBLE
}

fun AVLoadingIndicatorView.hideProgress() {
    this.visibility = View.GONE
}

fun Context.getUserDetails(): HashMap<String, String?> {
    val user = User(this)
    return user.getUserDetails()
}

fun Button.setEnabledState(enabled: Boolean) {
    isEnabled = enabled
    isClickable = enabled
    isActivated = enabled
}
