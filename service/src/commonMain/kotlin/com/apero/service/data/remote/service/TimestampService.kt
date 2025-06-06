package com.apero.service.data.remote.service

import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.TimestampResponseWrapper
import com.apero.service.extension.handleErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext


internal interface TimestampService {
    suspend fun getTimestamp(): ApiResult<TimestampResponseWrapper>
}

internal class TimestampServiceImpl(
    private val client: HttpClient
) : TimestampService {
    override suspend fun getTimestamp(): ApiResult<TimestampResponseWrapper> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = client.get("api/timestamp")
                ApiResult.Success(response.body())
            } catch (e: ClientRequestException) {
                e.response.handleErrorResponse()
            } catch (e: ServerResponseException) {
                e.response.handleErrorResponse()
            } catch (e: ResponseException) {
                e.response.handleErrorResponse()
            } catch (e: Exception) {
                ApiResult.Error(
                    message = "Network or unknown error: ${e.message}",
                    rawBody = null
                )
            }
        }
}
