package tech.ctawave.exoplayercmcd.repository

import androidx.lifecycle.LiveData
import tech.ctawave.exoplayercmcd.AppExecutors
import tech.ctawave.exoplayercmcd.api.MediaListService
import tech.ctawave.exoplayercmcd.database.MediaListDao
import tech.ctawave.exoplayercmcd.database.MediaListDatabase
import tech.ctawave.exoplayercmcd.util.RateLimiter
import tech.ctawave.exoplayercmcd.vo.Media
import tech.ctawave.exoplayercmcd.vo.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles available Media instances.
 */
@Singleton
class MediaListRepository @Inject constructor(private val appExecutors: AppExecutors, private val database: MediaListDatabase, private val mediaListDao: MediaListDao, private val mediaListService: MediaListService) {

    private val mediaListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadAllMedia(id: String): LiveData<Resource<List<Media>>> {
        return object: NetworkBoundResource<List<Media>, List<Media>>(appExecutors) {
            override fun saveCallResult(item: List<Media>) {
                mediaListDao.saveMedia(item)
            }

            override fun shouldFetch(data: List<Media>?): Boolean {
                return data == null || data.isEmpty() || mediaListRateLimit.shouldFetch(id)
            }

            override fun loadFromDatabase() = mediaListDao.loadAllMedia()

            override fun createCall() = mediaListService.getAllMedia()

            override fun onFetchFailed() {
                mediaListRateLimit.reset(id)
            }
        }.asLiveData()
    }
}
