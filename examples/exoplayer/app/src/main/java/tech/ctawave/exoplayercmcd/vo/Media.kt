package tech.ctawave.exoplayercmcd.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Media(@PrimaryKey private val id: String, val title: String, val uri: String)
