package com.ineedyourcode.groovymovie.utils

import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.db.HistoryEntity
import java.text.SimpleDateFormat
import java.util.*

fun convertHistoryEntityToMovie(entityList: List<HistoryEntity>): List<Int> {
    return entityList.map { it.id }
}

fun convertMovieToHistoryEntity(movie: Movie): HistoryEntity =
    HistoryEntity(movie.id, movie.title.toString(), getCurrentTime())

fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
    return dateFormat.format(Calendar.getInstance())
}
