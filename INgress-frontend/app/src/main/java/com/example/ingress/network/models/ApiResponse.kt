package com.example.ingress.network.models

import com.google.gson.annotations.SerializedName

/**
 * 通用API响应包装类
 * 根据API约定，所有接口返回的数据都将使用此格式
 */
data class ApiResponse<T>(
    @SerializedName("code")
    val code: Int, // HTTP状态码或业务错误码
    
    @SerializedName("message")
    val message: String, // 响应描述信息
    
    @SerializedName("data")
    val data: T? // 具体业务数据，可能为null
)