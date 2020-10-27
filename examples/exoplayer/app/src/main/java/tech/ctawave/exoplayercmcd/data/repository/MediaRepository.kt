package tech.ctawave.exoplayercmcd.data.repository

import androidx.lifecycle.LiveData
import tech.ctawave.exoplayercmcd.util.AppExecutors
import tech.ctawave.exoplayercmcd.util.RateLimiter
import tech.ctawave.exoplayercmcd.data.entities.Media
import tech.ctawave.exoplayercmcd.data.local.MediaDao
import tech.ctawave.exoplayercmcd.data.remote.MediaService
import tech.ctawave.exoplayercmcd.util.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles available Media instances.
 */
@Singleton
class MediaRepository @Inject constructor(private val appExecutors: AppExecutors, private val mediaDao: MediaDao, private val mediaService: MediaService) {

    private val mediaRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadMedia(id: String): LiveData<Resource<List<Media>>> {
        return object: NetworkBoundResource<List<Media>, List<Media>>(appExecutors) {
            override fun saveCallResult(item: List<Media>) {
                mediaDao.save(item)
            }

            override fun shouldFetch(data: List<Media>?): Boolean {
                return data == null || data.isEmpty() || mediaRateLimit.shouldFetch(id)
            }

            override fun loadFromDatabase() = mediaDao.getMedia()

            override fun createCall() = mediaService.getMedia()

            override fun onFetchFailed() {
                mediaRateLimit.reset(id)
            }
        }.asLiveData()
    }
}
