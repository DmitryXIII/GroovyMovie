package com.ineedyourcode.groovymovie.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey
    val id: Int,
    val movieTitle: String,
    val time: String
) {
}