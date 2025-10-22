package com.example.ingress.ui.screens


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.ingress.network.ApiClient
import com.example.ingress.network.ApiService
import java.util.Date
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import kotlinx.coroutines.CoroutineScope
import com.example.ingress.network.models.*
import com.example.ingress.utils.PreferenceManager
import com.example.ingress.navigation.Screen
import com.example.ingress.navigation.BottomNavBar
import java.text.SimpleDateFormat
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.os.Bundle
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.*
import com.amap.api.maps.model.*
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.CameraPosition
import androidx.core.app.ActivityCompat
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.example.ingress.network.models.AttackPortalRequest
import com.example.ingress.network.models.RepairPortalRequest
import com.example.ingress.network.models.AttackResultResponse
import com.example.ingress.network.models.RepairResultResponse
import androidx.compose.material3.Slider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults

/**
 * 加载据点信息
 */
fun loadLocations(
    context: Context,
    scope: CoroutineScope,
    apiService: ApiService,
    onSuccess: (List<LocationResponse>) -> Unit
) {
    scope.launch(Dispatchers.IO) {
        try {
            // 这里应该调用获取据点的API
            // 暂时使用空列表
            withContext(Dispatchers.Main) {
                onSuccess(emptyList())
            }

// 数据类
class Announcement(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Date
)

/**
 * 公告对话框组件
 */
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
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "加载据点失败: ${e.message}", Toast.LENGTH_SHORT).show()
                onSuccess(emptyList())
            }
        }
    }
}

/**
 * 加载据点的共振器信息
 */
fun loadResonators(
    context: Context,
    scope: CoroutineScope,
    apiService: ApiService,
    locationId: Int,
    onSuccess: (List<ResonatorResponse>) -> Unit
) {
    scope.launch(Dispatchers.IO) {
        try {
            // 这里应该调用获取共振器的API
            // 暂时使用空列表
            withContext(Dispatchers.Main) {
                onSuccess(emptyList())
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "加载共振器失败: ${e.message}", Toast.LENGTH_SHORT).show()
                onSuccess(emptyList())
            }
        }
    }
}

/**
 * 部署共振器
 */
suspend fun deployResonator(
    context: Context,
    apiService: ApiService,
    locationId: Int,
    slotNumber: Int,
    level: Int,
    onSuccess: () -> Unit
) {
    try {
        val deployRequest = DeployResonatorRequest(slotNumber, level)
        val response = apiService.deployResonator(locationId, deployRequest)

        withContext(Dispatchers.Main) {
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.code == 201 || apiResponse.code == 200) {
                    Toast.makeText(context, "共振器部署成功", Toast.LENGTH_SHORT).show()
                    onSuccess()
                } else {
                    Toast.makeText(context, apiResponse.message ?: "部署失败", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "网络请求失败", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "部署失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun AMapViewWithAutoLocation(
    modifier: Modifier = Modifier,
    locations: List<LocationResponse> = emptyList(), // 添加这个参数
    onMapLoaded: (AMap) -> Unit = {}
) {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var mapView by remember { mutableStateOf<MapView?>(null) }

    // 在 Composable 中初始化隐私合规
    LaunchedEffect(Unit) {
        AMapPrivacyUtil.initializePrivacy(context)

        // 检查权限并启动定位
        if (LocationUtil.hasLocationPermission(context)) {
            startAutoLocation(context) { latLng ->
                currentLocation = latLng
                Toast.makeText(context, "已定位到您的位置", Toast.LENGTH_SHORT).show()

                // 更新地图位置
                mapView?.map?.let { map ->
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    map.clear()

                    // 添加当前位置标记
                    map.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title("我的位置")
                            .snippet("实时定位")
                    )

                    // 添加测试据点标记
                    locations.forEach { location ->
                        val markerColor = when (location.ownerFactionId) {
                            1 -> BitmapDescriptorFactory.HUE_GREEN    // 启蒙军 - 绿色
                            2 -> BitmapDescriptorFactory.HUE_BLUE     // 抵抗军 - 蓝色
                            else -> BitmapDescriptorFactory.HUE_RED   // 中立 - 改为红色，更明显
                        }

                        map.addMarker(
                            MarkerOptions()
                                .position(LatLng(location.coordinate.latitude, location.coordinate.longitude))
                                .title(location.name)
                                .snippet(when (location.ownerFactionId) {
                                    1 -> "启蒙军据点"
                                    2 -> "抵抗军据点"
                                    else -> "中立据点"
                                })
                                .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                        )
                    }
                }
            }
        } else {
            // 没有权限，使用默认位置
            val defaultLocation = LatLng(30.2741, 120.1551) // 杭州
            currentLocation = defaultLocation
            Toast.makeText(context, "定位权限未开启，使用默认位置（杭州）", Toast.LENGTH_LONG).show()
        }
    }

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                onCreate(Bundle())
                mapView = this
                val map = getMap()

                // 地图配置
                map.mapType = AMap.MAP_TYPE_NORMAL

                // 设置UI控制
                val uiSettings = map.uiSettings
                uiSettings.isZoomControlsEnabled = true
                uiSettings.isCompassEnabled = true
                uiSettings.isScaleControlsEnabled = true
                uiSettings.isMyLocationButtonEnabled = true

                // 启用定位图层（显示蓝点）
                map.isMyLocationEnabled = true

                // 使用定位结果或默认位置
                val targetLatLng = currentLocation ?: LatLng(30.2741, 120.1551)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLatLng, 15f))

                onMapLoaded(map)
            }
        },
        update = { view ->
            // 当地图视图更新时，可以在这里处理
            currentLocation?.let { location ->
                val map = view.map
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
                map.clear()
                map.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title("我的位置")
                        .snippet(if (LocationUtil.hasLocationPermission(context)) "实时定位" else "默认位置")
                )
                map.isMyLocationEnabled = true
            }
        },
        modifier = modifier
    )
}

/**
 * 启动自动定位 - 修复版本
 */
private fun startAutoLocation(context: Context, onLocationResult: (LatLng) -> Unit) {
    try {
        val locationClient = AMapLocationClient(context.applicationContext)
        val locationOption = AMapLocationClientOption().apply {
            // 定位模式
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            // 是否返回地址信息
            isNeedAddress = true
            // 是否单次定位
            isOnceLocation = true
            // 是否允许模拟位置
            isMockEnable = false
            // 定位超时时间（毫秒）
            httpTimeOut = 30000
            // 定位间隔
            interval = 5000L
        }

        locationClient.setLocationOption(locationOption)

        locationClient.setLocationListener { location ->
            if (location.errorCode == 0) {
                // 定位成功
                val latLng = LatLng(location.latitude, location.longitude)
                android.util.Log.d("Location", "定位成功: $latLng")
                onLocationResult(latLng)

                // 定位成功后停止定位以节省电量
                locationClient.stopLocation()
                locationClient.onDestroy()
            } else {
                // 定位失败
                android.util.Log.e("Location", "定位失败: ${location.errorCode} - ${location.errorInfo}")
                // 不自动使用默认位置，让用户知道定位失败
                Toast.makeText(context, "定位失败: ${location.errorInfo}", Toast.LENGTH_LONG).show()
            }
        }

        // 启动定位
        locationClient.startLocation()

        // 30秒后强制停止定位
        android.os.Handler().postDelayed({
            try {
                locationClient.stopLocation()
                locationClient.onDestroy()
            } catch (e: Exception) {
                android.util.Log.e("Location", "停止定位失败: ${e.message}")
            }
        }, 30000)

    } catch (e: Exception) {
        android.util.Log.e("Location", "定位初始化失败: ${e.message}")
        Toast.makeText(context, "定位服务初始化失败", Toast.LENGTH_LONG).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val preferenceManager = remember { PreferenceManager(context) }
    val apiService = remember { ApiClient.getApiService() }

    // 添加定位状态
    var locationStatus by remember { mutableStateOf("正在定位...") }
    var hasLocationPermission by remember {
        mutableStateOf(LocationUtil.hasLocationPermission(context))
    }

    // 其他状态变量...
    var showPortalDetail by remember { mutableStateOf(false) }
    var showDeployDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var locations by remember {
        mutableStateOf<List<LocationResponse>>(
            listOf(
                LocationResponse(
                    id = 1,
                    name = "测试据点-西湖文化广场",
                    coordinate = Coordinate(30.2741, 120.1551),
                    ownerFactionId = 0, // 0表示中立
                    resonators = emptyList(),
                    totalHealth = 50,   // 修改为50点当前生命值
                    maxHealth = 100
                ),
                LocationResponse(
                    id = 2,
                    name = "测试据点-武林广场",
                    coordinate = Coordinate(30.2715, 120.1612),
                    ownerFactionId = 1, // 1表示启蒙军（绿色）
                    resonators = listOf(
                        ResonatorResponse(
                            id = 101,
                            locationId = 2,
                            deployerUserId = 1,
                            slotNumber = 1,
                            level = 5,
                            health = 100,
                            deployedAt = "2024-01-01T10:00:00"  // 添加时间
                        ),
                        ResonatorResponse(
                            id = 102,
                            locationId = 2,
                            deployerUserId = 1,
                            slotNumber = 2,
                            level = 4,
                            health = 80,
                            deployedAt = "2024-01-01T10:00:00"  // 添加时间
                        )
                    )
                ),
                LocationResponse(
                    id = 3,
                    name = "测试据点-杭州大厦",
                    coordinate = Coordinate(30.2698, 120.1587),
                    ownerFactionId = 2, // 2表示抵抗军（蓝色）
                    resonators = listOf(
                        ResonatorResponse(
                            id = 201,
                            locationId = 3,
                            deployerUserId = 2,
                            slotNumber = 1,
                            level = 6,
                            health = 90,
                            deployedAt = "2024-01-01T10:00:00"  // 添加时间
                        ),
                        ResonatorResponse(
                            id = 202,
                            locationId = 3,
                            deployerUserId = 2,
                            slotNumber = 3,
                            level = 5,
                            health = 70,
                            deployedAt = "2024-01-01T10:00:00"  // 添加时间
                        ),
                        ResonatorResponse(
                            id = 203,
                            locationId = 3,
                            deployerUserId = 2,
                            slotNumber = 5,
                            level = 4,
                            health = 100,
                            deployedAt = "2024-01-01T10:00:00"  // 添加时间
                        )
                    )
                )
            )
        )
    }

    var currentLocation by remember { mutableStateOf<LocationResponse?>(null) }
    var resonators by remember { mutableStateOf(emptyList<ResonatorResponse>()) }
    val currentRoute = "map"

    // 公告相关状态
    var showAnnouncementDialog by remember { mutableStateOf(false) }
    val announcements = remember { getMockAnnouncements() }

    // 用户数据状态
    var userLevel by remember { mutableStateOf(5) }
    var userExperience by remember { mutableStateOf(15000L) }
    var userFactionId by remember { mutableStateOf<Int?>(1) }
    var isAttacking by remember { mutableStateOf(false) }
    var isRepairing by remember { mutableStateOf(false) }
    var showAttackDialog by remember { mutableStateOf(false) }
    var showRepairDialog by remember { mutableStateOf(false) }
    var attackResult by remember { mutableStateOf<AttackResultResponse?>(null) }
    var repairResult by remember { mutableStateOf<RepairResultResponse?>(null) }

    // 攻防相关函数
    /**
     * 攻击后更新据点状态
     */
    fun updatePortalAfterAttack(portalId: Int, attackResult: AttackResultResponse) {
        locations = locations.map { location ->
            if (location.id == portalId) {
                // 更新所有者（如果被攻占）
                val newOwnerFactionId = attackResult.newOwnerFactionId ?: location.ownerFactionId

                // 更新共振器状态
                val updatedResonators = location.resonators.map { resonator ->
                    if (attackResult.destroyedResonators.contains(resonator.slotNumber)) {
                        resonator.copy(health = 0) // 被摧毁的共振器
                    } else {
                        resonator // 保持原样
                    }
                }.filter { it.health > 0 } // 移除被摧毁的共振器

                // 计算新的总生命值
                val newTotalHealth = updatedResonators.sumOf { it.health }

                location.copy(
                    ownerFactionId = newOwnerFactionId,
                    resonators = updatedResonators,
                    totalHealth = newTotalHealth
                )
            } else {
                location
            }
        }

        // 如果当前查看的据点被攻击了，也更新详情对话框
        if (currentLocation?.id == portalId) {
            currentLocation = locations.find { it.id == portalId }
            resonators = currentLocation?.resonators ?: emptyList()
        }
    }

    /**
     * 修复后更新据点状态
     */
    fun updatePortalAfterRepair(portalId: Int, resonatorSlot: Int, repairResult: RepairResultResponse) {
        locations = locations.map { location ->
            if (location.id == portalId) {
                val updatedResonators = location.resonators.map { resonator ->
                    if (resonator.slotNumber == resonatorSlot) {
                        resonator.copy(health = repairResult.resonatorHealth)
                    } else {
                        resonator
                    }
                }

                // 计算新的总生命值
                val newTotalHealth = updatedResonators.sumOf { it.health }

                location.copy(
                    resonators = updatedResonators,
                    totalHealth = newTotalHealth
                )
            } else {
                location
            }
        }

        // 更新当前查看的据点
        if (currentLocation?.id == portalId) {
            currentLocation = locations.find { it.id == portalId }
            resonators = currentLocation?.resonators ?: emptyList()
        }
    }

    // 攻防相关函数
    /**
     * 攻击据点
     */
    fun attackPortal(portalId: Int, damage: Int) {
        if (isAttacking) return

        isAttacking = true
        scope.launch {
            try {
                // 模拟攻击API调用
                val response = apiService.attackPortal(portalId, AttackPortalRequest(portalId, damage))

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!.data
                    attackResult = result

                    result?.let { attackResult ->
                        if (attackResult.success) {
                            // 更新本地据点状态
                            updatePortalAfterAttack(portalId, attackResult)
                            Toast.makeText(context, "攻击成功！造成${attackResult.damageDealt}点伤害", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, attackResult.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "攻击失败", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "攻击失败: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isAttacking = false
                showAttackDialog = false
            }
        }
    }

    /**
     * 修复据点
     */
    fun repairPortal(portalId: Int, resonatorSlot: Int, repairAmount: Int) {
        if (isRepairing) return

        isRepairing = true
        scope.launch {
            try {
                // 模拟修复API调用
                val response = apiService.repairPortal(portalId, RepairPortalRequest(resonatorSlot, repairAmount))

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!.data
                    repairResult = result

                    result?.let { repairResult ->
                        if (repairResult.success) {
                            // 更新本地据点状态
                            updatePortalAfterRepair(portalId, resonatorSlot, repairResult)
                            Toast.makeText(context, "修复成功！恢复${repairResult.repairedAmount}点生命值", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, repairResult.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "修复失败", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "修复失败: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isRepairing = false
                showRepairDialog = false
            }
        }
    }

    // 双重保障：在 MapScreen 中也初始化一次
    LaunchedEffect(Unit) {
        AMapPrivacyUtil.initializePrivacy(context)

        // 检查权限状态
        hasLocationPermission = LocationUtil.hasLocationPermission(context)
        locationStatus = if (hasLocationPermission) {
            "正在获取您的位置..."
        } else {
            "定位权限未开启，使用默认位置"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "据点地图", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = locationStatus,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // 添加定位权限状态图标
                    IconButton(onClick = {
                        // 点击可以显示定位状态信息
                        Toast.makeText(
                            context,
                            if (hasLocationPermission) "定位权限已开启"
                            else "定位权限未开启，请到设置中开启",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Icon(
                            Icons.Default.LocationOn,  // 始终使用 LocationOn 图标
                            contentDescription = "定位状态",
                            tint = if (hasLocationPermission) {
                                MaterialTheme.colorScheme.primary  // 有权限：主题色
                            } else {
                                MaterialTheme.colorScheme.error  // 无权限：错误色（红色）
                            }
                        )
                    }
                    IconButton(onClick = {
                        hasLocationPermission = LocationUtil.hasLocationPermission(context)
                        if (hasLocationPermission) {
                            locationStatus = "重新定位中..."
                        }
                        Toast.makeText(context, "刷新据点数据", Toast.LENGTH_SHORT).show()
                    }, enabled = !isLoading) {
                        Icon(Icons.Default.Refresh, contentDescription = "刷新")
                    }
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
    ) { innerPadding ->
        // 显示公告对话框
        if (showAnnouncementDialog) {
            AnnouncementDialog(
                announcements = announcements,
                onDismiss = { showAnnouncementDialog = false })
        }

        // 使用 Box 作为根布局
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                // 加载指示器
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "加载中...")
                }
            } else {
                // 地图内容
                Box(modifier = Modifier.fillMaxSize()) {
                    // 显示地图
                    AMapViewWithAutoLocation(
                        modifier = Modifier.fillMaxSize(),
                        locations = locations, // 传递测试据点数据
                        onMapLoaded = {
                            // 地图加载完成后的回调
                        }
                    )

                    // 在顶部显示定位和据点信息
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        // 定位状态
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                    MaterialTheme.shapes.medium
                                )
                                .padding(8.dp)
                        ) {
                            Text(
                                text = if (hasLocationPermission) "定位已开启" else "使用默认位置",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (hasLocationPermission) Color.Green else Color.Red
                            )
                        }
                    }

                    // 在底部显示据点标记
                    if (locations.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(bottom = 120.dp), // 调整底部间距避免被导航栏遮挡
                            horizontalArrangement = Arrangement.Center
                        ) {
                            locations.take(3).forEach { location ->
                                val markerColor = when (location.ownerFactionId) {
                                    1 -> Color(0xFF4CAF50) // 启蒙军 - 绿色
                                    2 -> Color(0xFF2196F3) // 抵抗军 - 蓝色
                                    else -> Color.Gray // 中立 - 灰色
                                }
                                PortalMarker(
                                    color = markerColor,
                                    onClick = {
                                        currentLocation = location
                                        resonators = location.resonators ?: emptyList()
                                        showPortalDetail = true
                                    }
                                )
                                Spacer(modifier = Modifier.width(24.dp))
                            }
                        }
                    } else {
                        // 如果没有据点显示提示
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 120.dp)
                        ) {
                            Text(
                                text = "当前区域暂无据点",
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                        MaterialTheme.shapes.medium
                                    )
                                    .padding(8.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

    // 据点详情对话框 - 添加攻防按钮
    if (showPortalDetail && currentLocation != null) {
        PortalDetailDialog(
            location = currentLocation!!,
            resonators = resonators,
            onDismiss = { showPortalDetail = false },
            onDeploy = { showDeployDialog = true },
            onAttack = {
                if (currentLocation != null) {
                    // 检查是否可以攻击（不能攻击自己阵营的据点）
                    if (currentLocation!!.ownerFactionId != userFactionId) {
                        showAttackDialog = true
                    } else {
                        Toast.makeText(context, "不能攻击自己阵营的据点", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onRepair = {
                if (currentLocation != null) {
                    // 检查是否可以修复（只能修复自己阵营的据点）
                    if (currentLocation!!.ownerFactionId == userFactionId) {
                        showRepairDialog = true
                    } else {
                        Toast.makeText(context, "只能修复自己阵营的据点", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            userFactionId = userFactionId
        )
    }

    // 部署共振器对话框
    if (showDeployDialog && currentLocation != null) {
        DeployResonatorDialog(
            onDismiss = { showDeployDialog = false },
            onConfirm = { slot, level ->
                scope.launch {
                    showDeployDialog = false
                    Toast.makeText(context, "部署共振器到插槽$slot，等级$level", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // 攻击据点对话框
    if (showAttackDialog && currentLocation != null) {
        AttackPortalDialog(
            portal = currentLocation!!,
            onDismiss = { showAttackDialog = false },
            onConfirm = { damage ->
                attackPortal(currentLocation!!.id, damage)
            },
            isAttacking = isAttacking
        )
    }

// 修复据点对话框
    if (showRepairDialog && currentLocation != null) {
        RepairPortalDialog(
            portal = currentLocation!!,
            resonators = resonators,
            onDismiss = { showRepairDialog = false },
            onConfirm = { slot, repairAmount ->
                repairPortal(currentLocation!!.id, slot, repairAmount)
            },
            isRepairing = isRepairing
        )
    }
}

@Composable
fun PortalMarker(color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color, shape = androidx.compose.foundation.shape.CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Place, contentDescription = "据点", tint = Color.White)
    }
}

// TopStatusBar函数已在ChatRoomScreen.kt中定义，这里不再重复

@Composable
fun BottomStatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "等级:5")
        Text(text = "EXP:15000/20000")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.Green, shape = androidx.compose.foundation.shape.CircleShape)
            )
            Text(text = "启蒙军", modifier = Modifier.padding(start = 4.dp))
        }
    }
}

@Composable
fun PortalDetailDialog(
    location: LocationResponse,
    resonators: List<ResonatorResponse>,
    onDismiss: () -> Unit,
    onDeploy: () -> Unit,
    onAttack: () -> Unit,
    onRepair: () -> Unit,
    userFactionId: Int?
) {
    val healthPercentage = if (location.maxHealth > 0) {
        (location.totalHealth.toFloat() / location.maxHealth.toFloat()) * 100
    } else {
        0f
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp).clip(MaterialTheme.shapes.large),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Place, contentDescription = null)
                        Text(
                            text = location.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 据点信息
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = when (location.ownerFactionId) {
                                    1 -> Color.Green // 启蒙军
                                    2 -> Color.Blue // 抵抗军
                                    else -> Color.Gray // 中立
                                },
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    Text(
                        text = when (location.ownerFactionId) {
                            1 -> "启蒙军"
                            2 -> "抵抗军"
                            else -> "中立"
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // 生命值显示
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "据点生命值: ${location.totalHealth}/${location.maxHealth}")
                LinearProgressIndicator(
                    progress = { healthPercentage / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(vertical = 4.dp),
                    color = when {
                        healthPercentage > 70 -> Color.Green
                        healthPercentage > 30 -> Color.Yellow
                        else -> Color.Red
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 共振器部署情况
                Text(text = "共振器部署情况", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                ResonatorSlots(resonators)

                Spacer(modifier = Modifier.height(24.dp))

                // 操作按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 部署按钮 - 始终显示
                    Button(
                        onClick = onDeploy,
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    ) {
                        Text("部署")
                    }

                    // 攻击按钮 - 只对敌方据点显示
                    if (location.ownerFactionId != userFactionId && location.ownerFactionId != 0) {
                        Button(
                            onClick = onAttack,
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text("攻击")
                        }
                    }

                    // 修复按钮 - 只对己方据点显示
                    if (location.ownerFactionId == userFactionId && location.ownerFactionId != 0) {
                        Button(
                            onClick = onRepair,
                            modifier = Modifier.weight(1f).padding(start = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green
                            )
                        ) {
                            Text("修复")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AttackPortalDialog(
    portal: LocationResponse,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    isAttacking: Boolean
) {
    var damage by remember { mutableStateOf(10) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp).clip(MaterialTheme.shapes.large),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "攻击据点",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "目标: ${portal.name}")
                Text(
                    text = "当前生命值: ${portal.totalHealth}/${portal.maxHealth}",
                    color = when {
                        portal.totalHealth > portal.maxHealth * 0.7 -> Color.Green
                        portal.totalHealth > portal.maxHealth * 0.3 -> Color.Yellow
                        else -> Color.Red
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "选择攻击强度")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "轻微")
                    Slider(
                        value = damage.toFloat(),
                        onValueChange = { damage = it.toInt() },
                        valueRange = 5f..50f,
                        steps = 8
                    )
                    Text(text = "强力")
                }
                Text(
                    text = "攻击伤害: $damage",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onConfirm(damage) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isAttacking,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    if (isAttacking) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text("确认攻击")
                    }
                }
            }
        }
    }
}

@Composable
fun RepairPortalDialog(
    portal: LocationResponse,
    resonators: List<ResonatorResponse>,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
    isRepairing: Boolean
) {
    var selectedSlot by remember { mutableStateOf(1) }
    var repairAmount by remember { mutableStateOf(10) }

    val damagedResonators = resonators.filter { it.health < 100 }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp).clip(MaterialTheme.shapes.large),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "修复据点",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "目标: ${portal.name}")
                Text(text = "当前生命值: ${portal.totalHealth}/${portal.maxHealth}")

                Spacer(modifier = Modifier.height(16.dp))

                if (damagedResonators.isEmpty()) {
                    Text(
                        text = "所有共振器状态良好，无需修复",
                        color = Color.Green,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    Text(text = "选择要修复的共振器")
                    LazyColumn(
                        modifier = Modifier.height(120.dp)
                    ) {
                        items(damagedResonators) { resonator ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .clickable { selectedSlot = resonator.slotNumber }, // 这里修复 slotNumber 引用
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedSlot == resonator.slotNumber)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "插槽 ${resonator.slotNumber}") // 这里修复 slotNumber 引用
                                    Text(text = "Lv.${resonator.level}")
                                    Text(
                                        text = "${resonator.health}%",
                                        color = when {
                                            resonator.health > 70 -> Color.Green
                                            resonator.health > 30 -> Color.Yellow
                                            else -> Color.Red
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "修复量")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "少量")
                        Slider(
                            value = repairAmount.toFloat(),
                            onValueChange = { repairAmount = it.toInt() },
                            valueRange = 5f..30f,
                            steps = 4
                        )
                        Text(text = "大量")
                    }
                    Text(
                        text = "修复量: $repairAmount",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onConfirm(selectedSlot, repairAmount) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isRepairing && damagedResonators.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    )
                ) {
                    if (isRepairing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text(if (damagedResonators.isEmpty()) "无需修复" else "确认修复")
                    }
                }
            }
        }
    }
}

@Composable
fun Grid(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
            content()
        }
    }
}

@Composable
fun ResonatorSlot(resonator: ResonatorResponse?, slotNumber: Int) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(
                if (resonator == null) Color.LightGray else Color.Green,
                shape = MaterialTheme.shapes.small
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center  // 在这里设置整个 Box 的内容对齐
    ) {
        if (resonator == null) {
            Text(text = "空")
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "${resonator.slotNumber}", fontSize = 10.sp)
                Text(text = "L${resonator.level}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = "${resonator.health}%", fontSize = 8.sp)
            }
        }
    }
}

@Composable
fun ResonatorSlots(resonators: List<ResonatorResponse>) {
    Grid(modifier = Modifier.fillMaxWidth()) {
        for (i in 1..8) {
            val resonator = resonators.find { it.slotNumber == i }
            ResonatorSlot(resonator = resonator, slotNumber = i)
        }
    }
}

@Composable
fun DeployResonatorDialog(onDismiss: () -> Unit, onConfirm: (Int, Int) -> Unit) {
    var selectedSlot by remember { mutableStateOf(1) }
    var selectedLevel by remember { mutableStateOf(5) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.padding(16.dp).clip(MaterialTheme.shapes.large),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "部署共振器",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "选择插槽")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (i in 1..8) {
                        Button(
                            onClick = { selectedSlot = i },
                            modifier = Modifier.size(40.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedSlot == i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text(text = "$i")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "选择等级")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (i in 1..8) {
                        Button(
                            onClick = { selectedLevel = i },
                            modifier = Modifier.size(40.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedLevel == i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text(text = "L$i")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "当前选择: 插槽${selectedSlot} - L${selectedLevel}级共振器", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onConfirm(selectedSlot, selectedLevel) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("确认部署")
                }
            }
        }
    }
}