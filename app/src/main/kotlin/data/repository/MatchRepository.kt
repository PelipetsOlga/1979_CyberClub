package com.application.data.repository

import com.application.domain.model.Match
import com.application.data.remote.api.MatchApiService
import com.application.data.remote.dto.MatchDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepository @Inject constructor(
    private val matchApiService: MatchApiService
) {
    suspend fun getMatches(): Result<List<Match>> {
        return try {
            val response = matchApiService.getMatches()
            if (response.isSuccessful && response.body() != null) {
                val matches = response.body()!!.map { it.toDomain() }
                Result.success(matches)
            } else {
                Result.failure(Exception("Failed to load matches: ${response.code()}"))
            }
        } catch (e: java.net.UnknownHostException) {
            // Network error - host not found
            Result.failure(Exception("Unable to load matches, try again later"))
        } catch (e: java.net.SocketTimeoutException) {
            // Network timeout
            Result.failure(Exception("Unable to load matches, try again later"))
        } catch (e: java.io.IOException) {
            // Network IO error
            Result.failure(Exception("Unable to load matches, try again later"))
        } catch (e: Exception) {
            // Other errors
            Result.failure(Exception("Unable to load matches, try again later"))
        }
    }
}

