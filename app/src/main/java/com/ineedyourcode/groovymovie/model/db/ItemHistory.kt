package com.ineedyourcode.groovymovie.model.db

/**
 * Класс описывает объект, хранящийся в истории просмотров информации о фильмах
 */
data class ItemHistory(
    val movieId: Int,
    val movieTitle: String,
    val posterPath: String,
    val date: String,
    val time: String
)