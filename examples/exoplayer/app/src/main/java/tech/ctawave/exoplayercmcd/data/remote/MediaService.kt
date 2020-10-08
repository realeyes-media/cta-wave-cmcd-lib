package tech.ctawave.exoplayercmcd.data.remote

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import tech.ctawave.exoplayercmcd.data.entities.Media

interface MediaService {
    @GET("media.json")
    fun getMedia(): LiveData<ApiResponse<List<Media>>>
}
