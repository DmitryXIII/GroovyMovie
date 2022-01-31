package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.ineedyourcode.groovymovie.model.tmdb.TMDBMovieDTO
import retrofit2.Callback

interface IRetrofitRepository {
    fun getTopRatedMovies(
        lang: String,
        page: Int
    )
}