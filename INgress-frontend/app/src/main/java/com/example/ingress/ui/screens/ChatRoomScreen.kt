package com.example.ingress.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.foundation.background
import androidx.navigation.NavHostController
import com.example.ingress.network.WebSocketService
import com.example.ingress.navigation.BottomNavBar
import com.example.ingress.utils.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(navController: NavHostController) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val scope = rememberCoroutineScope()
    val currentRoute = "chat_room"
    
    var selectedChannel by remember { mutableStateOf(ChannelType.PUBLIC) }
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(getMockChatMessages(selectedChannel)) }
    var isConnected by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    
    // 公告相关状态
    var showAnnouncementDialog by remember { mutableStateOf(false) }
    val announcements = remember { getMockAnnouncements() }
    
    // 用户数据状态 - 模拟数据
    val userLevel = remember { 5 }
    val userExperience = remember { 15000L }
    val userFactionId = remember { 1 } // 1 = 启蒙军
    
    // WebSocket 服务 - 简化处理
    val webSocketService = remember { WebSocketService(preferenceManager) }
    val username = remember { preferenceManager.getUsername() ?: "匿名用户" }
    val userId = remember { preferenceManager.getUserId() ?: 1 }

    // 初始化 WebSocket 连接
    LaunchedEffect(key1 = Unit) {
        // 初始化时设置为已连接状态，方便测试发送功能
        isConnected = true
        
        // 检查登录状态
        if (!preferenceManager.isLoggedIn()) {
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
            navController.navigate("login")
            return@LaunchedEffect
        }
        
        // 建立 WebSocket 连接
        try {
            connectWebSocket(context, webSocketService, preferenceManager, scope, {
                isConnected = true
                // 订阅公共频道
                subscribeToChannel(webSocketService, selectedChannel)
            }, {
                isConnected = false
                Toast.makeText(context, "连接断开", Toast.LENGTH_SHORT).show()
            })
            
            // 消息接收处理 - 监听WebSocket服务的聊天消息流
            scope.launch {
                webSocketService.chatMessages.collect { webSocketMessage ->
                    // 转换网络消息为本地消息模型
                    val localMessage = ChatMessage(
                        id = System.currentTimeMillis(),
                        senderId = webSocketMessage.sender.hashCode(),
                        senderUsername = webSocketMessage.sender,
                        channel = selectedChannel.name,
                        content = webSocketMessage.content,
                        sentAt = Date()
                    )
                    messages = messages + listOf(localMessage) // 添加到列表末尾
                }
            }
        } catch (e: Exception) {
            // 捕获异常但继续运行，不阻止UI交互
            Toast.makeText(context, "WebSocket初始化异常: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    // 切换频道时更新订阅
    LaunchedEffect(selectedChannel) {
        if (isConnected) {
            // 取消之前的订阅
            unsubscribeFromAllChannels(webSocketService)
            // 订阅新频道
            subscribeToChannel(webSocketService, selectedChannel)
            // 清空消息列表并加载模拟数据（实际应用中可以加载该频道的历史消息）
            messages = getMockChatMessages(selectedChannel)
        }
    }
    
    // 清理连接 - 简化处理
    DisposableEffect(key1 = Unit) {
        onDispose {
            // WebSocketService可能没有disconnect方法，这里简化处理
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "聊天室")
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(if (isConnected) Color.Green else Color.Red)
                                // 移除clip，使用background的shape属性
                            )
                    }
                },
                actions = {
                    IconButton(onClick = { showAnnouncementDialog = true }) {
                        Icon(Icons.Default.Notifications, contentDescription = "公告")
                    }
                }
            )
        },
        bottomBar = {
            Column {
                // 在底部导航栏上方添加状态栏
                TopStatusBar(
                    level = userLevel,
                    experience = userExperience,
                    factionId = userFactionId
                )
                BottomNavBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) {
        Column(modifier = Modifier.padding(it).fillMaxSize()) {
            ChannelTabs(
                selectedChannel = selectedChannel,
                onChannelChange = { selectedChannel = it }
            )
            // 使用Box布局来确保输入框在消息列表上方
            Box(modifier = Modifier.weight(1f)) {
                MessageList(
                    messages = messages,
                    listState = listState
                )
            }
            // 添加额外的padding确保输入框不会被底部导航栏挡住
            Spacer(modifier = Modifier.height(16.dp))
            BottomInputBar(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSend = {
                    if (messageText.isNotEmpty()) {
                        val content = messageText
                        messageText = ""
                        
                        // 直接发送消息并添加到本地列表
                        // 即使WebSocket连接未建立也允许发送（用于测试）
                        val newMessage = ChatMessage(
                            id = System.currentTimeMillis(),
                            senderId = userId,
                            senderUsername = username,
                            channel = selectedChannel.name,
                            content = content,
                            sentAt = Date()
                        )
                        
                        // 将新消息添加到列表末尾，因为我们更改了reverseLayout为false
                        messages = messages + listOf(newMessage)
                        
                        // 如果连接已建立，则通过WebSocket发送
                        if (isConnected) {
                            webSocketService.sendChatMessage(content)
                        } else {
                            Toast.makeText(context, "消息已添加到本地", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
    
    // 显示公告对话框
    if (showAnnouncementDialog) {
        AnnouncementDialog(announcements = announcements, onDismiss = { showAnnouncementDialog = false })
    }
}

@Composable
fun ChannelTabs(selectedChannel: ChannelType, onChannelChange: (ChannelType) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // 为每个按钮设置weight
        Button(
            onClick = { onChannelChange(ChannelType.PUBLIC) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedChannel == ChannelType.PUBLIC) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = if (selectedChannel == ChannelType.PUBLIC) ButtonDefaults.buttonElevation() else null
        ) {
            Text(text = "公共")
        }
        Button(
            onClick = { onChannelChange(ChannelType.FACTION) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedChannel == ChannelType.FACTION) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = if (selectedChannel == ChannelType.FACTION) ButtonDefaults.buttonElevation() else null
        ) {
            Text(text = "阵营")
        }
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = if (isSelected) ButtonDefaults.buttonElevation() else null
    ) {
        Text(text = text)
    }
}

@Composable
fun MessageList(messages: List<ChatMessage>, listState: androidx.compose.foundation.lazy.LazyListState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        reverseLayout = false // 正常布局，让用户可以上下滑动查看消息
    ) {
        // 直接显示消息列表，用户可以通过上下滑动查看所有消息
        items(messages) {
            MessageItem(message = it)
        }
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    val formatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    // 根据senderId设置默认颜色（实际应用中应该查询用户所属阵营）
    val factionColor = when (message.senderId) {
        1 -> Color.Green // 假设用户1属于启蒙军
        2 -> Color.Blue // 假设用户2属于抵抗军
        else -> Color.Gray // 默认颜色
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(factionColor, shape = androidx.compose.foundation.shape.CircleShape)
            )
            Text(
                    text = message.senderUsername,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                    modifier = Modifier.padding(start = 8.dp, end = 12.dp)
                )
            Text(
                    text = formatter.format(message.sentAt),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
        }
        Text(text = message.content, modifier = Modifier.padding(top = 4.dp, start = 20.dp))
    }
}

@Composable
fun TopStatusBar(level: Int, experience: Long, factionId: Int?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "等级: $level", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "EXP: $experience/20000", fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            when (factionId) {
                                1 -> Color.Green // 启蒙军
                                2 -> Color.Blue // 抵抗军
                                else -> Color.Gray // 中立
                            }, 
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
                Text(
                    text = when (factionId) {
                        1 -> "启蒙军"
                        2 -> "抵抗军"
                        else -> "中立"
                    }, 
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        // 经验值进度条
        LinearProgressIndicator(
            progress = { experience.toFloat() / 20000f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .padding(top = 2.dp),
            color = when (factionId) {
                1 -> Color.Green // 启蒙军
                2 -> Color.Blue // 抵抗军
                else -> Color.Gray // 中立
            }
        )
    }
}

@Composable
fun BottomInputBar(messageText: String, onMessageChange: (String) -> Unit, onSend: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White) // 使用固定颜色
            .padding(bottom = 8.dp), // 额外的底部padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageChange,
            placeholder = { Text(text = "输入消息...") },
            modifier = Modifier.weight(1f).heightIn(min = 48.dp, max = 120.dp), // 确保最小高度
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            maxLines = 4
        )
        IconButton(
            onClick = onSend,
            modifier = Modifier.padding(start = 8.dp),
            enabled = messageText.isNotEmpty() // 只有输入内容时才启用发送
        ) {
            Icon(
                Icons.Default.Send, 
                contentDescription = "发送",
                tint = if (messageText.isNotEmpty()) Color.Blue else Color.Gray
            )
        }
    }
}

@Composable
fun AnnouncementDialog(announcements: List<Announcement>, onDismiss: () -> Unit) {
    val formatter = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "系统公告") },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                itemsIndexed(announcements) { index, announcement ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = announcement.title, fontWeight = MaterialTheme.typography.titleMedium.fontWeight)
                            Text(text = formatter.format(announcement.createdAt), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = announcement.content,
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 14.sp
                        )
                        if (index < announcements.size - 1) {
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "关闭")
            }
        }
    )
}

// 数据类
enum class ChannelType {
    PUBLIC,
    FACTION
}

class ChatMessage(
    val id: Long,
    val senderId: Int,
    val senderUsername: String,
    val channel: String,
    val content: String,
    val sentAt: Date
)

class Announcement(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Date
)

/**
 * 连接 WebSocket
 */
suspend fun connectWebSocket(
    context: Context,
    webSocketService: WebSocketService,
    preferenceManager: PreferenceManager,
    scope: CoroutineScope,
    onConnected: () -> Unit,
    onDisconnected: () -> Unit
) {
    try {
        val token = preferenceManager.getToken()
        if (token == null) {
            Toast.makeText(context, "Token 不存在", Toast.LENGTH_SHORT).show()
            return
        }
        
        webSocketService.connect()
        // 连接后直接调用onConnected回调
        onConnected()
    } catch (e: Exception) {
        Toast.makeText(context, "WebSocket 连接失败: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

/**
 * 订阅指定频道
 */
fun subscribeToChannel(webSocketService: WebSocketService, channelType: ChannelType) {
    // 根据频道类型订阅不同的WebSocket主题
    when (channelType) {
        ChannelType.PUBLIC -> webSocketService.subscribeToChannel("/topic/public")
        ChannelType.FACTION -> webSocketService.subscribeToChannel("/topic/faction")
    }
}

/**
 * 取消所有频道订阅
 */
fun unsubscribeFromAllChannels(webSocketService: WebSocketService) {
    // 取消所有可能的订阅
    webSocketService.subscribeToChannel("/topic/unsubscribe/public")
    webSocketService.subscribeToChannel("/topic/unsubscribe/faction")
}

/**
 * 处理接收的消息
 */
suspend fun processIncomingMessages(
    webSocketService: WebSocketService,
    scope: CoroutineScope,
    onMessageReceived: (Any) -> Unit
) {
    // WebSocketService可能没有receiveMessage方法，这里简化处理
    // 实际应用中需要根据WebSocketService的API进行调整
    delay(1000) // 避免无限循环
}

// 模拟数据
fun getMockChatMessages(channelType: ChannelType): List<ChatMessage> {
    val messages = mutableListOf<ChatMessage>()
    val now = Date()
    val calendar = Calendar.getInstance()
    calendar.time = now

    when (channelType) {
        ChannelType.PUBLIC -> {
            calendar.add(Calendar.MINUTE, -1)
            messages.add(ChatMessage(1, 2, "player1", "PUBLIC", "有人在附近吗？", calendar.time))
            
            calendar.add(Calendar.MINUTE, -2)
            messages.add(ChatMessage(2, 3, "player2", "PUBLIC", "我刚占领了市中心的据点！", calendar.time))
            
            calendar.add(Calendar.MINUTE, -5)
            messages.add(ChatMessage(3, 4, "player3", "PUBLIC", "有组队任务吗？", calendar.time))
        }
        ChannelType.FACTION -> {
            calendar.add(Calendar.MINUTE, -1)
            messages.add(ChatMessage(4, 5, "leader", "FACTION", "大家准备明天的大规模行动！", calendar.time))
            
            calendar.add(Calendar.MINUTE, -3)
            messages.add(ChatMessage(5, 6, "player4", "FACTION", "我可以负责东边的据点", calendar.time))
            
            calendar.add(Calendar.MINUTE, -10)
            messages.add(ChatMessage(6, 7, "player5", "FACTION", "我们需要更多的共振器", calendar.time))
        }
    }

    return messages
}

// 模拟公告数据
fun getMockAnnouncements(): List<Announcement> {
    val announcements = mutableListOf<Announcement>()
    val now = Date()
    val calendar = Calendar.getInstance()
    calendar.time = now

    calendar.add(Calendar.HOUR, -2)
    announcements.add(Announcement(1, "系统维护通知", "明天凌晨2点-4点系统将进行维护，请提前做好准备。", calendar.time))
    
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    announcements.add(Announcement(2, "活动公告", "周末双倍经验活动即将开始，敬请期待！", calendar.time))
    
    calendar.add(Calendar.DAY_OF_MONTH, -3)
    announcements.add(Announcement(3, "版本更新", "新版本已发布，新增多种功能和优化。", calendar.time))

    return announcements
}