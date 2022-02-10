package com.ineedyourcode.groovymovie.utils

import android.annotation.SuppressLint
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import java.text.SimpleDateFormat
import java.util.*

// HISTORY ENTITY
fun convertMovieToHistoryEntity(movie: Movie): HistoryEntity =
    HistoryEntity(
        0,
        movie.id,
        movie.title.toString(),
        movie.posterPath.toString(),
        getCurrentDate(),
        getCurrentTime()
    )

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

// FAVORITE ENTITY
fun convertMovieToFavoriteEntity(movie: Movie) = FavoriteEntity (movie.id, movie.title, movie.rating, movie.posterPath)

// NOTE ENTITY
fun convertMovieToNoteEntity(movie: Movie, noteContent: String): NotesEntity =
    NotesEntity(movie.id, movie.title.toString(), noteContent)

fun convertNoteEntityToString(note: NotesEntity): NotesEntity = note
