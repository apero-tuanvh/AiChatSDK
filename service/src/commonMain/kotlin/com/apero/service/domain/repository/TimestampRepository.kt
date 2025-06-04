package com.apero.service.domain.repository

interface TimestampRepository {
    suspend fun getTimestamp(): Long
}