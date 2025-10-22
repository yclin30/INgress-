package com.example.ingress.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BottomNavBar(navController: NavHostController, currentRoute: String) {
    NavigationBar {
        // 左侧：地图游戏
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "地图游戏") },
            label = { Text("地图游戏") },
            selected = currentRoute == "map",
            onClick = { if (currentRoute != "map") navController.navigate("map") }
        )
        // 中间：聊天室
        NavigationBarItem(
            icon = { Icon(Icons.Default.Phone, contentDescription = "聊天室") },
            label = { Text("聊天室") },
            selected = currentRoute == "chat_room",
            onClick = { if (currentRoute != "chat_room") navController.navigate("chat_room") }
        )
        // 右侧：个人信息
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "个人信息") },
            label = { Text("个人信息") },
            selected = currentRoute == "profile",
            onClick = { if (currentRoute != "profile") navController.navigate("profile") }
        )
    }
}