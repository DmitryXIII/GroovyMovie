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

    fun getTopRatedList(
        lang: String,
        page: Int,
        callback: Callback<TmdbResponse.ResponseMoviesList>
    ) {
        tmdbAPI.getTopRated(lang = lang, page = page).enqueue(callback)
    }

    fun getGenresList(lang: String, callback: Callback<TmdbResponse.ResponseGenres>) {
        tmdbAPI.getGenres(lang = lang).enqueue(callback)
    }

    fun getMovieById(id: Int, lang: String, callback: Callback<TmdbMovieByIdDTO>) {
        tmdbAPI.getMovieByID(id = id, lang = lang).enqueue(callback)
    }
}
