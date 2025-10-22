package com.example.ingress.network

import com.example.ingress.network.models.ApiResponse
import com.example.ingress.network.models.PagedResponse
import com.example.ingress.network.models.RegisterRequest
import com.example.ingress.network.models.LoginRequest
import com.example.ingress.network.models.AuthResponse
import com.example.ingress.network.models.LocationResponse
import com.example.ingress.network.models.DeployResonatorRequest
import com.example.ingress.network.models.ResonatorResponse
import com.example.ingress.network.models.CreateLocationRequest
import com.example.ingress.network.models.CreateAnnouncementRequest
import com.example.ingress.network.models.UserResponse
import com.example.ingress.network.models.UpdateUserRequest
// 添加攻防相关的模型导入
import com.example.ingress.network.models.AttackPortalRequest
import com.example.ingress.network.models.RepairPortalRequest
import com.example.ingress.network.models.AttackResultResponse
import com.example.ingress.network.models.RepairResultResponse
import com.example.ingress.network.models.PortalStatusResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // 认证模块
    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<Nothing>>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    // 用户模块
    @GET("/users/me")
    suspend fun getCurrentUser(): Response<ApiResponse<UserResponse>>

    @PUT("/users/me")
    suspend fun updateCurrentUser(@Body request: UpdateUserRequest): Response<ApiResponse<UserResponse>>

    // 游戏核心模块
    @GET("/locations")
    suspend fun getLocations(
        @Query("minLat") minLat: Double,
        @Query("minLon") minLon: Double,
        @Query("maxLat") maxLat: Double,
        @Query("maxLon") maxLon: Double
    ): Response<ApiResponse<List<LocationResponse>>>

    @POST("/locations/{locationId}/resonators")
    suspend fun deployResonator(
        @Path("locationId") locationId: Int,
        @Body request: DeployResonatorRequest
    ): Response<ApiResponse<ResonatorResponse>>

    // === 添加攻防相关接口 ===

    /**
     * 攻击据点
     */
    @POST("/locations/{locationId}/attack")
    suspend fun attackPortal(
        @Path("locationId") locationId: Int,
        @Body request: AttackPortalRequest
    ): Response<ApiResponse<AttackResultResponse>>

    /**
     * 修复据点
     */
    @POST("/locations/{locationId}/repair")
    suspend fun repairPortal(
        @Path("locationId") locationId: Int,
        @Body request: RepairPortalRequest
    ): Response<ApiResponse<RepairResultResponse>>

    /**
     * 获取据点状态
     */
    @GET("/locations/{locationId}/status")
    suspend fun getPortalStatus(
        @Path("locationId") locationId: Int
    ): Response<ApiResponse<PortalStatusResponse>>

    /**
     * 获取据点的共振器列表
     */
    @GET("/locations/{locationId}/resonators")
    suspend fun getPortalResonators(
        @Path("locationId") locationId: Int
    ): Response<ApiResponse<List<ResonatorResponse>>>

    // 管理员模块
    @GET("/admin/users")
    suspend fun getUsers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ApiResponse<PagedResponse<UserResponse>>>

    @POST("/admin/users/{userId}/ban")
    suspend fun banUser(@Path("userId") userId: Int): Response<ApiResponse<Nothing>>

    @POST("/admin/users/{userId}/unban")
    suspend fun unbanUser(@Path("userId") userId: Int): Response<ApiResponse<Nothing>>

    @POST("/admin/locations")
    suspend fun createLocation(@Body request: CreateLocationRequest): Response<ApiResponse<LocationResponse>>

    @POST("/admin/announcements")
    suspend fun createAnnouncement(@Body request: CreateAnnouncementRequest): Response<ApiResponse<Nothing>>
}