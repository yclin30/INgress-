package com.example.ingress.network

import android.content.Context
import com.example.ingress.utils.PreferenceManager

import com.example.ingress.network.models.ApiResponse
import com.example.ingress.network.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * API客户端配置
 */
object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/" // 使用Android模拟器的localhost别名
    private lateinit var apiService: ApiService

    /**
     * 初始化API服务
     */
    fun initialize(context: Context) {
        val preferenceManager = PreferenceManager(context)
        
        // 创建OkHttpClient
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(preferenceManager))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        // 创建Retrofit实例
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    /**
     * 获取API服务实例
     */
    fun getApiService(): ApiService {
        if (!::apiService.isInitialized) {
            throw IllegalStateException("ApiClient must be initialized first")
        }
        return apiService
    }
}

/**
 * 认证拦截器，用于添加Authorization头
 */
class AuthInterceptor(private val preferenceManager: PreferenceManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = preferenceManager.getToken()

        // 如果有token，则添加Authorization头
        if (!token.isNullOrEmpty()) {
            val requestWithAuth = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            return chain.proceed(requestWithAuth)
        }

        return chain.proceed(originalRequest)
    }
}

// ApiService接口已在单独的ApiService.kt文件中定义