package com.apero.service.domain.usecase

import com.apero.service.domain.repository.TimestampRepository

class GetTimestampUseCase(
    private val repository: TimestampRepository
) {
    suspend operator fun invoke(): Long {
        val result = repository.getTimestamp()
        return result
    }
}