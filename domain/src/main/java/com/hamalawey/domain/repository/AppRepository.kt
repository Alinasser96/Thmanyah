package com.hamalawey.domain.repository

import androidx.paging.PagingData
import com.hamalawey.domain.model.Section
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun getHomeSections(): Result<List<Section>>
    suspend fun searchContent(
        query: String?,
        type: String?,
        page: Int? = null,
        limit: Int? = null
    ): Result<List<Section>>
}