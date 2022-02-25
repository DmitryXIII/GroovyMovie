package com.ineedyourcode.groovymovie.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotesEntity(
    @PrimaryKey
    val movieId: Int,
    val movieTitle: String,
    val noteContent: String
)