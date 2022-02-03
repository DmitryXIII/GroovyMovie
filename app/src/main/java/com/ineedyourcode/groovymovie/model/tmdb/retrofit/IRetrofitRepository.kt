package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieByIdDTO
import retrofit2.Callback

interface IRetrofitRepository {
    fun getMoviesList(moviesListType: String, callback: Callback<TmdbResponse.ResponseMoviesList>)
    fun getGenresList(callback: Callback<TmdbResponse.ResponseGenres>)
    fun getMovieById(callback: Callback<TmdbMovieByIdDTO>)
}