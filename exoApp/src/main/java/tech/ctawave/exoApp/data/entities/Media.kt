package tech.ctawave.exoApp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "media")
@Serializable
data class Media(@PrimaryKey val id: String = "", val title: String, val uri: String, val format: String)
