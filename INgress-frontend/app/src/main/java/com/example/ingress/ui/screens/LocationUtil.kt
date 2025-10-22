package com.example.ingress.ui.screens

import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.amap.api.location.AMapLocationClientOption

/**
 * 高德地图定位工具类
 */
object LocationUtil {

    /**
     * 检查位置权限
     */
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查定位服务是否开启
     */
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * 获取定位配置
     */
    fun getLocationOption(): AMapLocationClientOption {
        return AMapLocationClientOption().apply {
            // 定位模式
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            // 是否返回地址信息
            isNeedAddress = true
            // 是否单次定位
            isOnceLocation = true
            // 是否允许模拟位置
            isMockEnable = false
            // 定位间隔
            interval = 2000L
            // 定位超时时间
            httpTimeOut = 30000L
            // 是否强制刷新WIFI
            isWifiScan = true
            // 是否使用传感器
            isSensorEnable = true
        }
    }
}