package com.ineedyourcode.groovymovie.model.db

data class ItemHistory(
    val movieId: Int,
    val movieTitle: String,
    val posterPath: String,
    val date: String,
    val time: String
)