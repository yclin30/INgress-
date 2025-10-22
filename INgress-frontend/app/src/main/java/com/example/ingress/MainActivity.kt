package com.example.ingress

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.ingress.network.ApiClient
import com.example.ingress.navigation.AppNavigation
import com.example.ingress.ui.theme.INgressTheme
import com.example.ingress.ui.screens.AMapPrivacyUtil

class MainActivity : ComponentActivity() {
    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // 使用新的权限请求合约
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            // 权限授予成功
            Toast.makeText(this, "定位权限已获取", Toast.LENGTH_SHORT).show()
        } else {
            // 权限被拒绝
            Toast.makeText(this, "定位权限被拒绝，将使用默认位置", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 初始化高德地图隐私合规
        AMapPrivacyUtil.initializePrivacy(this)

        // 检查并请求定位权限
        if (!hasLocationPermissions()) {
            requestLocationPermissions()
        }

        // 初始化API客户端
        ApiClient.initialize(this)

        setContent {
            INgressTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainApp()
                }
            }
        }
    }

    private fun hasLocationPermissions(): Boolean {
        return locationPermissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(locationPermissions)
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    AppNavigation(navController = navController)
}