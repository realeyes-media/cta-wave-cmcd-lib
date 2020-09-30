package tech.ctawave.exoplayercmcd.api

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import tech.ctawave.exoplayercmcd.vo.Media

interface MediaListService {
    @GET("/media/all")
    fun getAllMedia(): LiveData<ApiResponse<List<Media>>>
}
