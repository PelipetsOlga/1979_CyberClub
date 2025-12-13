package com.application.data.remote.api

import com.application.data.remote.dto.MatchDto
import retrofit2.Response
import retrofit2.http.GET

interface MatchApiService {
    @GET("matches")
    suspend fun getMatches(): Response<List<MatchDto>>
}

