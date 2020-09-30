package tech.ctawave.exoplayercmcd.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.ctawave.exoplayercmcd.vo.Media

@Dao
interface MediaListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMedia(media: List<Media>)

    @Query("SELECT * FROM media")
    fun loadAllMedia(): LiveData<List<Media>>
}
