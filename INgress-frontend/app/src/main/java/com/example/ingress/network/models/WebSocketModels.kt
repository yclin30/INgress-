package com.example.ingress.network.models

import com.google.gson.annotations.SerializedName

/**
 * WebSocket聊天消息
 */
class ChatMessage(
    @SerializedName("sender") val sender: String,
    @SerializedName("content") val content: String,
    @SerializedName("type") val type: String = "CHAT"
)

/**
 * WebSocket公告消息
 */
class AnnouncementMessage(
    @SerializedName("content") val content: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("type") val type: String = "ANNOUNCEMENT"
)