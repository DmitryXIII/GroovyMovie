package com.ineedyourcode.groovymovie.viewmodel

import com.ineedyourcode.groovymovie.model.Movie

sealed class AppState {
    data class Success(val moviesData: Map<String, Movie>, val genresData: List<String>) : AppState()
    data class Error(val error: String): AppState()
    object Loading: AppState()
}