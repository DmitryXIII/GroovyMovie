package com.ineedyourcode.groovymovie.viewmodel

import com.ineedyourcode.groovymovie.model.Movie

sealed class AppState {
    data class Success(val moviesData: Map<String, Movie>) : AppState()
    data class Error(val e: String): AppState()
    object Loading: AppState()
}