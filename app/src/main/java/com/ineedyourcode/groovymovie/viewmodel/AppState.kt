package com.ineedyourcode.groovymovie.viewmodel

import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbActorDto
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDto

sealed class AppState {
    data class MoviesListSuccess(val moviesData: List<TmdbMovieByIdDto>) : AppState()
    data class ActorsByIdSuccess(val actorDto: TmdbActorDto) : AppState()
    data class MovieByIdSuccess(val movieDto: TmdbMovieByIdDto) : AppState()
    data class HistorySuccess(val history: List<HistoryEntity>) : AppState()
    data class FavoriteListSuccess(val favoriteList: List<TmdbMovieByIdDto>) : AppState()
    data class IsFavoriteSuccess(val isFavorite: Boolean) : AppState()
    data class NoteSuccess(val note: NotesEntity) : AppState()
    data class Error(val e: String): AppState()
    object Loading: AppState()
}