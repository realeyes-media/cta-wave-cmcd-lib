package tech.ctawave.exoApp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.ctawave.exoApp.data.entities.Media

@Dao
interface MediaDao {
    @Query("SELECT * FROM media")
    suspend fun getMedia(): List<Media>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(media: List<Media>)
}
