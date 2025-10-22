package com.example.ingress.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.ingress.network.ApiClient
import com.example.ingress.network.models.LoginRequest
import com.example.ingress.network.models.RegisterRequest
import com.example.ingress.navigation.Screen
import com.example.ingress.utils.PreferenceManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val scope = rememberCoroutineScope()
    
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showRegisterDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // 检查是否已登录
    LaunchedEffect(key1 = Unit) {
        if (preferenceManager.isLoggedIn()) {
            navController.navigate(Screen.Map.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "INGRESS",
            fontSize = 36.sp,
            fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { 
                username = it 
                errorMessage = ""
            },
            label = { Text("用户名") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it 
                errorMessage = ""
            },
            label = { Text("密码") },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                TextButton(onClick = { showPassword = !showPassword }) {
                    Text(text = if (showPassword) "隐藏" else "显示")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it }
            )
            Text("记住登录状态")
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = { 
                isLoading = true
                // 处理登录逻辑
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    scope.launch {
                        loginUser(context, navController, preferenceManager, username, password)
                        isLoading = false
                    }
                } else {
                    errorMessage = "请输入用户名和密码"
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(vertical = 16.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("登录", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        TextButton(onClick = { showRegisterDialog = true }) {
            Text("注册账号")
        }
    }

    if (showRegisterDialog) {
        RegisterDialog(
            onDismiss = { showRegisterDialog = false },
            onRegisterSuccess = { 
                showRegisterDialog = false
                Toast.makeText(context, "注册成功，请登录", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

/**
 * 登录用户
 */
suspend fun loginUser(
    context: Context,
    navController: NavHostController,
    preferenceManager: PreferenceManager,
    username: String,
    password: String
) {
    // 测试用户验证逻辑
    if (username == "admin1" && password == "123456") {
        // 模拟登录成功
        val mockToken = "mock_token_" + System.currentTimeMillis()
        preferenceManager.saveToken(mockToken)
        preferenceManager.saveUsername(username)
        Toast.makeText(context, "测试用户登录成功", Toast.LENGTH_SHORT).show()
        
        // 导航到地图页面
        navController.navigate(Screen.Map.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
        return
    }
    
    try {
        val response = ApiClient.getApiService().login(LoginRequest(username, password))
        if (response.isSuccessful) {
            val authResponse = response.body()?.data
            if (authResponse != null) {
                // 保存Token
                preferenceManager.saveToken(authResponse.token)
                preferenceManager.saveUsername(username)
                
                // 导航到地图页面
                navController.navigate(Screen.Map.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        } else {
            val errorMessage = response.body()?.message ?: "登录失败，请检查用户名和密码"
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    } catch (e: HttpException) {
        Toast.makeText(context, "服务器错误，请稍后重试", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        Toast.makeText(context, "网络错误，请检查网络连接", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "登录失败: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterDialog(onDismiss: () -> Unit, onRegisterSuccess: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var newUsername by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<String?>(null) }
    var introduction by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }
    var registerError by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    
    val genderMapping = mapOf(
        "男性" to "MALE",
        "女性" to "FEMALE",
        "其他" to "OTHER",
        "保密" to "PREFER_NOT_TO_SAY"
    )
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "注册新账号",
                        fontSize = 20.sp,
                        fontWeight = MaterialTheme.typography.headlineMedium.fontWeight
                    )
                    TextButton(onClick = onDismiss) {
                        Text(text = "X")
                    }
                }

                OutlinedTextField(
                    value = newUsername,
                    onValueChange = { 
                        newUsername = it 
                        registerError = ""
                    },
                    label = { Text("用户名") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it 
                        registerError = ""
                    },
                    label = { Text("邮箱地址") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { 
                        newPassword = it 
                        registerError = ""
                    },
                    label = { Text("密码") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    trailingIcon = {
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(text = if (showPassword) "隐藏" else "显示")
                        }
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { 
                        confirmPassword = it 
                        registerError = ""
                    },
                    label = { Text("确认密码") },
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    trailingIcon = {
                        TextButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Text(text = if (showConfirmPassword) "隐藏" else "显示")
                        }
                    },
                    singleLine = true
                )

                Text("性别 (可选)", modifier = Modifier.padding(vertical = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    genderMapping.keys.forEach { genderOption ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = gender == genderOption,
                                onClick = { gender = genderOption }
                            )
                            Text(text = genderOption, modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }

                OutlinedTextField(
                    value = introduction,
                    onValueChange = { introduction = it },
                    label = { Text("个人介绍") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(120.dp),
                    maxLines = 4
                )

                if (registerError.isNotEmpty()) {
                    Text(
                        text = registerError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Button(
                    onClick = { 
                        // 处理注册逻辑
                        registerError = ""
                        
                        if (newUsername.isEmpty() || email.isEmpty() || newPassword.isEmpty()) {
                            registerError = "请填写必填字段"
                            return@Button
                        }
                        
                        if (newPassword != confirmPassword) {
                            registerError = "两次输入的密码不一致"
                            return@Button
                        }
                        
                        if (newPassword.length < 6) {
                            registerError = "密码长度至少为6位"
                            return@Button
                        }
                        
                        scope.launch {
                            isRegistering = true
                            try {
                                val genderValue = gender?.let { genderMapping[it] }
                                val response = ApiClient.getApiService().register(
                                    RegisterRequest(
                                        username = newUsername,
                                        email = email,
                                        password = newPassword,
                                        gender = genderValue,
                                        introduction = if (introduction.isNotEmpty()) introduction else null
                                    )
                                )
                                
                                if (response.isSuccessful) {
                                    onRegisterSuccess()
                                } else {
                                    registerError = response.body()?.message ?: "注册失败"
                                }
                            } catch (e: HttpException) {
                                registerError = "服务器错误，请稍后重试"
                            } catch (e: IOException) {
                                registerError = "网络错误，请检查网络连接"
                            } catch (e: Exception) {
                                registerError = "注册失败: ${e.message}"
                            } finally {
                                isRegistering = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp).padding(top = 16.dp),
                    enabled = !isRegistering
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        if (isRegistering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("确认注册")
                        }
                    }
                }
            }
        }
    }
}