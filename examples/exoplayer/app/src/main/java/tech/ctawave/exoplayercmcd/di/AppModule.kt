package tech.ctawave.exoplayercmcd.di

import android.app.Application
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import tech.ctawave.exoplayercmcd.data.local.AppDatabase
import tech.ctawave.exoplayercmcd.data.local.MediaDao
import tech.ctawave.exoplayercmcd.data.remote.MediaService
import tech.ctawave.exoplayercmcd.util.LiveDataCallAdapterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {
    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideMediaListService(): MediaService {
        return Retrofit.Builder()
            .baseUrl("https://static.realeyes.cloud/cmcd/")
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(MediaService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "media")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMediaDao(database: AppDatabase): MediaDao {
        return database.mediaDao()
    }
}
