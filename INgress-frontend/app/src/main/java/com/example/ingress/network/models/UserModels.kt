package com.example.ingress.network.models

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * 用户响应
 */
class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("gender") val gender: String?,
    @SerializedName("introduction") val introduction: String?,
    @SerializedName("level") val level: Int,
    @SerializedName("experience") val experience: Long,
    @SerializedName("is_banned") val isBanned: Boolean,
    @SerializedName("faction") val faction: FactionResponse?,
    @SerializedName("created_at") val createdAt: String
)

/**
 * 阵营响应
 */
class FactionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

/**
 * 更新用户请求
 */
class UpdateUserRequest(
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("introduction") val introduction: String? = null,
    @SerializedName("faction_id") val factionId: Int? = null
)