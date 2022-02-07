package com.ineedyourcode.groovymovie.utils

import android.annotation.SuppressLint
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.db.ItemHistory
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import java.text.SimpleDateFormat
import java.util.*

fun convertHistoryEntityToMovie(entityList: List<HistoryEntity>): List<ItemHistory> {
    return entityList.map {
        ItemHistory(
            it.movieId,
            it.movieTitle,
            it.posterPath,
            it.date,
            it.time
        )
    }
}

fun convertMovieToHistoryEntity(movie: Movie): HistoryEntity =
    HistoryEntity(
        0,
        movie.id,
        movie.title.toString(),
        movie.posterPath.toString(),
        getCurrentDate(),
        getCurrentTime()
    )

fun convertMovieToNoteEntity(movie: Movie, noteContent: String): NotesEntity =
    NotesEntity(movie.id, movie.title.toString(), noteContent)

fun convertNoteEntityToString(note: NotesEntity): NotesEntity {
    return note
}

@SuppressLint("SimpleDateFormat")
fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss")
    return dateFormat.format(Date())
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    return dateFormat.format(Date())
}
