package com.example.ingress.network.models

import com.google.gson.annotations.SerializedName

/**
 * 坐标类
 */
data class Coordinate(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)

/**
 * 位置响应
 */
data class LocationResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("coordinate")
    val coordinate: Coordinate,
    @SerializedName("ownerFactionId")
    val ownerFactionId: Int?,
    @SerializedName("resonators")
    val resonators: List<ResonatorResponse> = emptyList(),  // 添加这一行
    @SerializedName("totalHealth")  // 添加这个字段
    val totalHealth: Int = 100,       // 总生命值
    @SerializedName("maxHealth")    // 添加这个字段
    val maxHealth: Int = 100          // 最大生命值
)

/**
 * 共振器响应
 */
data class ResonatorResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("locationId")
    val locationId: Int,
    @SerializedName("deployerUserId")
    val deployerUserId: Int,
    @SerializedName("slotNumber")
    val slotNumber: Int,
    @SerializedName("level")
    val level: Int,
    @SerializedName("health")
    val health: Int,
    @SerializedName("deployedAt")
    val deployedAt: String
)

/**
 * 共振器部署请求模型
 */
data class DeployResonatorRequest(
    @SerializedName("slot_number")
    val slotNumber: Int,
    @SerializedName("level")
    val level: Int
)

/**
 * 创建位置请求
 */
data class CreateLocationRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)

/**
 * 创建公告请求
 */
data class CreateAnnouncementRequest(
    @SerializedName("content")
    val content: String
)

/**
 * 攻击据点请求
 */
data class AttackPortalRequest(
    val portalId: Int,
    val damage: Int
)

/**
 * 修复据点请求
 */
data class RepairPortalRequest(
    @SerializedName("resonator_slot")
    val resonatorSlot: Int,
    @SerializedName("repair_amount")
    val repairAmount: Int
)

/**
 * 据点状态响应
 */
data class PortalStatusResponse(
    val portalId: Int,
    val ownerFactionId: Int?,
    val totalHealth: Int,
    val maxHealth: Int,
    val resonators: List<ResonatorResponse>,
    val isUnderAttack: Boolean,
    val lastAttackTime: String?
)

/**
 * 攻击结果响应
 */
data class AttackResultResponse(
    val success: Boolean,
    val damageDealt: Int,
    val destroyedResonators: List<Int>, // 被摧毁的共振器插槽
    val newOwnerFactionId: Int?,
    val message: String
)

/**
 * 修复结果响应
 */
data class RepairResultResponse(
    val success: Boolean,
    val repairedAmount: Int,
    val resonatorHealth: Int,
    val message: String
)