package tech.ctawave.exoplayercmcd.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.ctawave.exoplayercmcd.data.entities.Media

@Dao
interface MediaDao {
    @Query("SELECT * FROM media")
    fun getMedia(): LiveData<List<Media>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(media: List<Media>)
}
