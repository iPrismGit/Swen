package com.iprism.swen.utils

import android.app.Activity
import android.util.Log
import com.cashfree.pg.api.CFPaymentGatewayService
import com.cashfree.pg.base.exception.CFException
import com.cashfree.pg.core.api.CFSession
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback
import com.cashfree.pg.core.api.utils.CFErrorResponse
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutPayment
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutTheme

class PaymentManager(
    private val activity: Activity,
    private val listener: PaymentResultListener
) : CFCheckoutResponseCallback {

    var cfEnvironment = CFSession.Environment.PRODUCTION

    init {
        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this)
        } catch (e: CFException) {
            e.printStackTrace()
        }
    }

    fun startPayment(orderId: String, paymentSessionId: String) {
        try {
            val cfSession = CFSession.CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionId)
                .setOrderId(orderId)
                .build()

            val cfTheme = CFWebCheckoutTheme.CFWebCheckoutThemeBuilder()
                .setNavigationBarBackgroundColor("#000000")
                .setNavigationBarTextColor("#FFFFFF")
                .build()

            val cfWebCheckoutPayment = CFWebCheckoutPayment.CFWebCheckoutPaymentBuilder()
                .setSession(cfSession)
                .setCFWebCheckoutUITheme(cfTheme)
                .build()

            CFPaymentGatewayService.getInstance().doPayment(activity, cfWebCheckoutPayment)
        } catch (e: CFException) {
            e.printStackTrace()
            listener.onFailure("Payment Init Failed: ${e.localizedMessage}")
        }
    }

    override fun onPaymentVerify(orderID: String) {
        Log.d("PaymentManager", "Verify triggered for $orderID")
        listener.onSuccess(orderID)
    }

    override fun onPaymentFailure(cfErrorResponse: CFErrorResponse, orderID: String) {
        Log.e("PaymentManager", "Payment Failed $orderID ${cfErrorResponse.message}")
        listener.onFailure("Payment Failed: ${cfErrorResponse.message}")
    }

    interface PaymentResultListener {
        fun onSuccess(orderId: String)
        fun onFailure(error: String)
    }
}
