package com.example.ingress.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.ScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.ingress.navigation.BottomNavBar
import com.example.ingress.utils.PreferenceManager
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ProfileScreen(navController: NavHostController) {
    val currentRoute = "profile"
    var showEditDialog by remember { mutableStateOf(false) }
    var userData by remember { mutableStateOf(UserData(
        id = 1,
        username = "admin1",
        email = "admin@example.com",
        role = "ROLE_ADMIN",
        gender = "男性",
        introduction = "大家好！我是这个游戏的忠实玩家，喜欢探索城市中的各个据点。",
        level = 5,
        experience = 15000,
        factionId = 1 // 启蒙军ID
    )) }

    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val scrollState = remember { ScrollState(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "个人中心", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "编辑")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp)
                .padding(bottom = 80.dp) // 增加底部padding确保按钮不被导航栏遮挡
        ) {
            ProfileHeader(user = userData)
            Spacer(modifier = Modifier.height(24.dp))
            InfoCards(user = userData)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 登出按钮
            Button(
                onClick = {
                    // 清除用户Token
                    preferenceManager.clearToken()
                    // 显示提示
                    Toast.makeText(context, "已登出", Toast.LENGTH_SHORT).show()
                    // 导航到登录页面
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336) // 红色登出按钮
                )
            ) {
                Text(text = "登出账号", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showEditDialog) {
        EditProfileDialog(
            user = userData,
            onDismiss = { showEditDialog = false },
            onSave = { newUserData ->
                userData = newUserData
                showEditDialog = false
            }
        )
    }
}

@Composable
fun ProfileHeader(user: UserData) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 头像
        Box(
            modifier = Modifier
                .size(120.dp)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = "头像", modifier = Modifier.fillMaxSize())
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 用户信息
        Text(
            text = user.username,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "(Lv.${user.level})",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(16.dp)
            )
            Text(
                text = " ${when(user.factionId) {
                    1 -> "启蒙军"
                    2 -> "抵抗军"
                    else -> "中立"
                }} · ${user.role}",
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun InfoCards(user: UserData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 基本信息卡片
        InfoCard(title = "基本信息") {
            InfoRow(label = "用户名:", value = user.username)
            InfoRow(label = "邮箱:", value = user.email)
            InfoRow(label = "性别:", value = user.gender ?: "保密")
            InfoRow(label = "注册时间:", value = user.createdAt.toString())
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 游戏数据卡片
        InfoCard(title = "游戏数据") {
            InfoRow(label = "等级:", value = "${user.level}")
            InfoRow(label = "经验值:", value = "${user.experience}/20000")
            
            // 经验值进度条
            LinearProgressIndicator(
                progress = { user.experience.toFloat() / 20000f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .padding(vertical = 8.dp)
            )
            
            InfoRow(label = "部署共振器:", value = "24个") // 模拟数据
            InfoRow(label = "占领据点:", value = "8个") // 模拟数据
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 个人介绍卡片
        InfoCard(title = "个人介绍") {
            Text(text = user.introduction ?: "暂无介绍", modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
fun InfoCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun EditProfileDialog(user: UserData, onDismiss: () -> Unit, onSave: (UserData) -> Unit) {
    var gender by remember { mutableStateOf(user.gender ?: "") }
    var introduction by remember { mutableStateOf(user.introduction ?: "") }
    var factionId by remember { mutableStateOf(user.factionId) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "编辑个人资料",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(onClick = {
                        onSave(UserData(
                            id = user.id,
                            username = user.username,
                            email = user.email,
                            passwordHash = user.passwordHash,
                            role = user.role,
                            gender = gender,
                            introduction = introduction,
                            level = user.level,
                            experience = user.experience,
                            isBanned = user.isBanned,
                            factionId = factionId,
                            createdAt = user.createdAt
                        ))
                    }) {
                        Text(text = "保存")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 性别选择
                Text(text = "性别", fontWeight = FontWeight.Bold)
                TextField(
                    value = gender,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 个人介绍
                Text(text = "个人介绍", fontWeight = FontWeight.Bold)
                TextField(
                    value = introduction,
                    onValueChange = { introduction = it },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 阵营选择
                Text(text = "选择阵营:", fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth()) {
                    RadioButton(
                        selected = factionId == 1,
                        onClick = { factionId = 1 },
                        enabled = user.factionId == null // 只有未选择阵营时才可更改
                    )
                    Text(text = "🟢 启蒙军 - 追求知识与发展", modifier = Modifier.padding(start = 8.dp))
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    RadioButton(
                        selected = factionId == 2,
                        onClick = { factionId = 2 },
                        enabled = user.factionId == null
                    )
                    Text(text = "🔵 抵抗军 - 守护传统与稳定", modifier = Modifier.padding(start = 8.dp))
                }
                Text(
                    text = "⚠️ 注意: 阵营通常只能选择一次",
                    color = Color(0xFFFF9800), // 使用警告色的十六进制值
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

// 数据类
class UserData(
    val id: Int = 0,
    val username: String = "admin1",
    val email: String = "admin@example.com",
    val passwordHash: String = "", // 实际应用中不应该在UI层展示密码哈希
    val role: String = "ROLE_ADMIN",
    val gender: String? = "男性",
    val introduction: String? = "大家好！我是这个游戏的忠实玩家，喜欢探索城市中的各个据点。",
    val level: Int = 5,
    val experience: Long = 15000,
    val isBanned: Boolean = false,
    val factionId: Int? = 1,
    val createdAt: Date = Date()
)