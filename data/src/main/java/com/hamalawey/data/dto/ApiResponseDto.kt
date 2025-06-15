package com.hamalawey.data.dto

data class ApiResponseDto(
    val sections: List<SectionDto>,
    val pagination: PaginationDto
)
