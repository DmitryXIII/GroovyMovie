package com.ineedyourcode.groovymovie.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val movieId: Int,
    val movieTitle: String,
    val posterPath: String,
    val date: String,
    val time: String
)