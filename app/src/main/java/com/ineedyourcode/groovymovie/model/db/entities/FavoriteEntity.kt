package com.ineedyourcode.groovymovie.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEntity(
    @PrimaryKey
    val movieId: Int,
    val movieTitle: String?,
    val rating: String?,
    val posterPath: String?,
    val releaseDate: String?
)