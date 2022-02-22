package com.ineedyourcode.groovymovie.viewmodel

import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbActorDto
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO

sealed class AppState {
    data class MoviesListSuccess(val moviesData: Map<Int, Movie>) : AppState()
    data class ActorsByIdSuccess(val actorDto: TmdbActorDto) : AppState()
    data class MovieByIdSuccess(val movieDto: TmdbMovieByIdDTO) : AppState()
    data class Error(val e: String): AppState()
    object Loading: AppState()
}