package tech.ctawave.exoplayercmcd.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import tech.ctawave.exoplayercmcd.api.MediaListService
import tech.ctawave.exoplayercmcd.database.MediaListDao
import tech.ctawave.exoplayercmcd.database.MediaListDatabase
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideMediaListService(): MediaListService {
        return Retrofit.Builder()
            .baseUrl("https://static.realeyes.cloud/")
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(MediaListService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(app: Application): MediaListDatabase {
        return Room.databaseBuilder(app, MediaListDatabase::class.java, "medialist.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMediaListDao(database: MediaListDatabase): MediaListDao {
        return database.mediaListDao()
    }
}
