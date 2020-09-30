package tech.ctawave.exoplayercmcd.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.ctawave.exoplayercmcd.vo.Media

@Database(entities = [Media::class], version = 3, exportSchema = false)
abstract class MediaListDatabase: RoomDatabase() {
    abstract fun mediaListDao(): MediaListDao
}
