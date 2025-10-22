package com.example.ingress.ui.screens

import android.content.Context
import com.amap.api.maps.MapsInitializer

/**
 * 高德地图隐私合规工具类
 */
object AMapPrivacyUtil {

    /**
     * 初始化高德地图隐私合规设置
     * 必须在任何地图操作之前调用
     */
    fun initializePrivacy(context: Context) {
        try {
            // 更新隐私权政策显示状态
            MapsInitializer.updatePrivacyShow(context, true, true)
            // 更新隐私权政策同意状态
            MapsInitializer.updatePrivacyAgree(context, true)
            android.util.Log.d("AMapPrivacy", "高德地图隐私合规初始化成功")
        } catch (e: Exception) {
            android.util.Log.e("AMapPrivacy", "隐私合规初始化失败: ${e.message}")
        }
    }
}