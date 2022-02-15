package com.ineedyourcode.groovymovie.utils

import android.annotation.SuppressLint
import androidx.annotation.DimenRes
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO
import java.text.SimpleDateFormat
import java.util.*

// HISTORY ENTITY
fun convertMovieToHistoryEntity(movie: TmdbMovieByIdDTO): HistoryEntity =
    HistoryEntity(
        0,
        movie.id,
        movie.title,
        movie.posterPath,
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
// ========================================================================

// FAVORITE ENTITY
fun convertMovieToFavoriteEntity(movie: TmdbMovieByIdDTO) =
    FavoriteEntity(movie.id, movie.title, movie.voteAverage.toString(), movie.posterPath, movie.releaseDate)
// ========================================================================

// NOTE ENTITY
fun convertMovieToNoteEntity(movie: TmdbMovieByIdDTO, noteContent: String): NotesEntity =
    NotesEntity(movie.id, movie.title, noteContent)
// ========================================================================

fun convertDpToPixels(resources: android.content.res.Resources, @DimenRes resourceId: Int) =
    resources.getDimensionPixelSize(resourceId)
