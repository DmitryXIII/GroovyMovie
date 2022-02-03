package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.google.gson.GsonBuilder
import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieByIdDTO
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {

    private val tmdbAPI = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build().create(MovieAPI::class.java)

    fun getMoviesList(
        moviesListType: String,
        callback: Callback<TmdbResponse.ResponseMoviesList>
    ) {
        tmdbAPI.getMoviesList(moviesListType).enqueue(callback)
    }

    fun getGenresList(callback: Callback<TmdbResponse.ResponseGenres>) {
        tmdbAPI.getGenres().enqueue(callback)
    }

    fun getMovieById(callback: Callback<TmdbMovieByIdDTO>) {
        tmdbAPI.getMovieByID().enqueue(callback)
    }
}
