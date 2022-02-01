package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import retrofit2.Callback

interface IRetrofitRepository {
    fun getTopRatedMovies(lang: String, page: Int, callback: Callback<TmdbResponse.ResponseMoviesList>)
    fun getGenresList(lang: String, callback: Callback<TmdbResponse.ResponseGenres>)
    fun getMovie(id: Int, lang: String)
}