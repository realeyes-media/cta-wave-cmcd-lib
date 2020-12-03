package tech.ctawave.exoApp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.ctawave.exoApp.data.entities.Media

@Database(entities = [Media::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}
