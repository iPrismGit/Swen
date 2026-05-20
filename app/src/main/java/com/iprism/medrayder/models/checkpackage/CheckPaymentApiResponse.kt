package com.iprism.medrayder.models.checkpackage

import com.google.gson.annotations.SerializedName

data class CheckPaymentApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class OrderMeta(

	@field:SerializedName("payment_methods")
	val paymentMethods: Any,

	@field:SerializedName("payment_methods_filters")
	val paymentMethodsFilters: Any,

	@field:SerializedName("return_url")
	val returnUrl: Any,

	@field:SerializedName("notify_url")
	val notifyUrl: Any
)

data class OneClickCheckout(

	@field:SerializedName("conditions")
	val conditions: List<Any>,

	@field:SerializedName("enabled")
	val enabled: Boolean
)

data class VerifyPay(

	@field:SerializedName("conditions")
	val conditions: List<Any>,

	@field:SerializedName("enabled")
	val enabled: Boolean
)

data class CustomerDetails(

	@field:SerializedName("customer_uid")
	val customerUid: Any,

	@field:SerializedName("customer_email")
	val customerEmail: String,

	@field:SerializedName("customer_phone")
	val customerPhone: String,

	@field:SerializedName("customer_name")
	val customerName: Any,

	@field:SerializedName("customer_id")
	val customerId: String
)

data class Response(

	@field:SerializedName("cf_order_id")
	val cfOrderId: String,

	@field:SerializedName("order_meta")
	val orderMeta: OrderMeta,

	@field:SerializedName("order_currency")
	val orderCurrency: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("products")
	val products: Products,

	@field:SerializedName("order_note")
	val orderNote: Any,

	@field:SerializedName("order_status")
	val orderStatus: String,

	@field:SerializedName("customer_details")
	val customerDetails: CustomerDetails,

	@field:SerializedName("order_expiry_time")
	val orderExpiryTime: String,

	@field:SerializedName("order_splits")
	val orderSplits: List<Any>,

	@field:SerializedName("order_amount")
	val orderAmount: String,

	@field:SerializedName("cart_details")
	val cartDetails: Any,

	@field:SerializedName("payment_session_id")
	val paymentSessionId: String,

	@field:SerializedName("terminal_data")
	val terminalData: Any,

	@field:SerializedName("order_id")
	val orderId: String,

	@field:SerializedName("entity")
	val entity: String,

	@field:SerializedName("order_tags")
	val orderTags: Any
)

data class Products(

	@field:SerializedName("verify_pay")
	val verifyPay: VerifyPay,

	@field:SerializedName("one_click_checkout")
	val oneClickCheckout: OneClickCheckout
)
