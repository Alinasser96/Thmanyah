package com.hamalawey.data.dto

import com.google.gson.annotations.SerializedName

data class PaginationDto(
    @SerializedName("next_page")
    val nextPage: String?,
    @SerializedName("total_pages")
    val totalPages: Int
)
