package com.ineedyourcode.groovymovie.viewmodel

import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbActorDto
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO

sealed class AppState {
    data class MoviesListSuccess(val moviesData: List<TmdbMovieByIdDTO>) : AppState()
    data class ActorsByIdSuccess(val actorDto: TmdbActorDto) : AppState()
    data class MovieByIdSuccess(val movieDto: TmdbMovieByIdDTO) : AppState()
    data class HistorySuccess(val history: List<HistoryEntity>) : AppState()
    data class FavoriteListSuccess(val favoriteList: List<FavoriteEntity>) : AppState()
    data class NoteSuccess(val note: NotesEntity) : AppState()
    data class Error(val e: String): AppState()
    object Loading: AppState()
}