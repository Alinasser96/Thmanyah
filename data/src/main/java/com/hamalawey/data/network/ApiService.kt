package com.hamalawey.data.network

import com.hamalawey.data.dto.ApiResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("home_sections")
    suspend fun getHomeSections(): ApiResponseDto

    @GET("https://mock.apidog.com/m1/735111-711675-default/search") // New endpoint
    suspend fun searchContent(
        @Query("query") query: String?,    // Make nullable if the API allows empty queries
        @Query("type") type: String?,      // Optional type filter
        @Query("page") page: Int?,         // Optional pagination
        @Query("limit") limit: Int?        // Optional pagination
    ): ApiResponseDto
}