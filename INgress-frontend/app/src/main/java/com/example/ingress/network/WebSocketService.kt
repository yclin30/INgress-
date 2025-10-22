package com.example.ingress.network

import android.util.Log
import com.example.ingress.network.models.ChatMessage
import com.example.ingress.network.models.AnnouncementMessage
import com.example.ingress.utils.PreferenceManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

/**
 * WebSocket服务，用于处理聊天和公告功能
 */
class WebSocketService(private val preferenceManager: PreferenceManager) {
    private val TAG = "WebSocketService"
    private val gson = Gson()
    private var webSocket: WebSocket? = null
    
    // 聊天消息流
    private val _chatMessages = MutableSharedFlow<ChatMessage>()
    val chatMessages: SharedFlow<ChatMessage> = _chatMessages
    
    // 公告消息流
    private val _announcements = MutableSharedFlow<AnnouncementMessage>()
    val announcements: SharedFlow<AnnouncementMessage> = _announcements
    
    // 是否已连接
    private var isConnected = false

    /**
     * 连接WebSocket
     */
    fun connect() {
        val token = preferenceManager.getToken()
        if (token.isNullOrEmpty()) {
            Log.e(TAG, "Cannot connect to WebSocket: No authentication token")
            return
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("ws://your-server-address/ws") // 替换为实际WebSocket地址
            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, "WebSocket connected")
                isConnected = true
                
                // 连接成功后订阅默认频道
                subscribeToChannel("/topic/public")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(TAG, "Received message: $text")
                
                try {
                    // 尝试解析为聊天消息
                    val chatMessage = gson.fromJson(text, ChatMessage::class.java)
                    if (chatMessage.type == "CHAT") {
                        CoroutineScope(Dispatchers.Main).launch {
                            _chatMessages.emit(chatMessage)
                        }
                    } else if (chatMessage.type == "ANNOUNCEMENT") {
                        // 解析为公告消息
                        val announcement = gson.fromJson(text, AnnouncementMessage::class.java)
                        CoroutineScope(Dispatchers.Main).launch {
                            _announcements.emit(announcement)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing WebSocket message: $e")
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(TAG, "WebSocket closing: $reason")
                isConnected = false
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(TAG, "WebSocket closed: $reason")
                isConnected = false
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                super.onFailure(webSocket, t, response)
                Log.e(TAG, "WebSocket failure: ${t.message}", t)
                isConnected = false
            }
        })
    }

    /**
     * 断开WebSocket连接
     */
    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
        isConnected = false
    }

    /**
     * 订阅频道
     */
    fun subscribeToChannel(topic: String) {
        if (!isConnected) {
            Log.e(TAG, "Cannot subscribe: WebSocket not connected")
            return
        }

        val subscribeMessage = "{\"command\":\"subscribe\",\"destination\":\"$topic\"}"
        webSocket?.send(subscribeMessage)
        Log.d(TAG, "Subscribed to channel: $topic")
    }

    /**
     * 发送聊天消息
     */
    fun sendChatMessage(content: String) {
        if (!isConnected) {
            Log.e(TAG, "Cannot send message: WebSocket not connected")
            return
        }

        val chatMessage = gson.toJson(mapOf(
            "content" to content
        ))
        val message = "{\"command\":\"send\",\"destination\":\"/app/chat.sendMessage\",\"payload\":$chatMessage}"
        webSocket?.send(message)
        Log.d(TAG, "Sent chat message: $content")
    }
}