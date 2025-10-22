package com.example.ingress.network.models

import com.google.gson.annotations.SerializedName

/**
 * 分页响应
 */
class PagedResponse<T>(
    @SerializedName("content") val content: List<T>,
    @SerializedName("page") val page: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("last") val isLast: Boolean
)