package tech.ctawave.exoApp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tech.ctawave.exoApp.util.AppExecutors
import tech.ctawave.exoApp.util.RateLimiter
import tech.ctawave.exoApp.data.entities.Media
import tech.ctawave.exoApp.data.local.MediaDao
import tech.ctawave.exoApp.data.remote.ApiResponse
import tech.ctawave.exoApp.data.remote.MediaService
import tech.ctawave.exoApp.util.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles available Media instances.
 */
@Singleton
class MediaRepository @Inject constructor(private val appExecutors: AppExecutors, private val mediaDao: MediaDao, private val mediaService: MediaService) {

    private val mediaRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadMedia(id: String): Flow<Resource<List<Media>>> {
        return object: NetworkBoundResource<List<Media>, List<Media>>(appExecutors) {
            override fun saveCallResult(item: List<Media>) {
                mediaDao.save(item)
            }

            override fun shouldFetch(data: List<Media>?): Boolean {
                return data == null || data.isEmpty() || mediaRateLimit.shouldFetch(id)
            }

            override fun loadFromDatabase(): Flow<List<Media>> {
                return flow {
                    emit(mediaDao.getMedia())
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<Media>>> {
                return mediaService.getMedia()
            }

            override fun onFetchFailed() {
                mediaRateLimit.reset(id)
            }
        }.asFlow()
    }
}
