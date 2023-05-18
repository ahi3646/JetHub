package com.hasan.jetfasthub.networking

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(
    private var token: String? = null,
    private var otp: String? = null
) : Interceptor {

    private var isScrapping: Boolean = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val original =chain.request()
        val builder =original.newBuilder()

        //here you do stuffs related to enterprise auth
        //val isEnterprise = LinkParserHelper.isEnterprise(original.url.host)
        val authToken = token
        val otpCode = otp
        if (!authToken.isNullOrBlank()) {
            builder.header("Authorization", if (authToken.startsWith("Basic")) authToken else "token $authToken")
        }
        if (!otpCode.isNullOrBlank()) {
            builder.addHeader("X-GitHub-OTP", otpCode.trim())
        }
        if (!isScrapping) builder.addHeader("User-Agent", "FastHub")
        val request = builder.build()
        return chain.proceed(request)
    }
}