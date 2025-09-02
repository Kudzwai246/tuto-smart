package com.tuto

object PaymentRepository {
    const val PAYMENT_SERVER_ENDPOINT = "__PAYMENT_SERVER_ENDPOINT__"

    suspend fun startMockPayment(provider: String, amountUsd: Double): Boolean {
        return true
    }
}
