package com.example.ingress.network.models

import com.google.gson.annotations.SerializedName

/**
 * 注册请求
 */
class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("introduction") val introduction: String? = null
)

/**
 * 登录请求
 */
class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

/**
 * 认证响应
 */
class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("tokenType") val tokenType: String
)