package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieByIdDTO
import retrofit2.Callback

interface IRetrofitRepository {
    fun getTopRatedMovies(lang: String, page: Int, callback: Callback<TmdbResponse.ResponseMoviesList>)
    fun getGenresList(lang: String, callback: Callback<TmdbResponse.ResponseGenres>)
    fun getMovieById(id: Int, lang: String, callback: Callback<TmdbMovieByIdDTO>)
}