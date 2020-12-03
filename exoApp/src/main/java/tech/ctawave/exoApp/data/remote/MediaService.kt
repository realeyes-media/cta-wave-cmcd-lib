package tech.ctawave.exoApp.data.remote

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import tech.ctawave.exoApp.data.entities.Media

interface MediaService {
    @GET("media.json")
    fun getMedia(): Flow<ApiResponse<List<Media>>>
}
