package com.ineedyourcode.groovymovie.viewmodel

import com.ineedyourcode.groovymovie.model.Movie
import java.lang.Exception

sealed class AppState {
    data class Success(val moviesData: Map<String, Movie>, val genresData: Set<String>) : AppState()
    data class Error(val e: Exception): AppState()
    object Loading: AppState()
}