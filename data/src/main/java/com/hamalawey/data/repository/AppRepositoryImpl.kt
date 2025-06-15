package com.hamalawey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hamalawey.data.mapper.toDomain
import com.hamalawey.data.network.ApiService
import com.hamalawey.domain.model.Section
import com.hamalawey.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AppRepository {
    override suspend fun getHomeSections(): Result<List<Section>> {
        return try {
            val responseDto = apiService.getHomeSections()
            Result.success(responseDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchContent(
        query: String?,
        type: String?,
        page: Int?,
        limit: Int?
    ): Result<List<Section>> {
        return try {
            val responseDto = apiService.searchContent(query, type, page, limit)
            Result.success(responseDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}