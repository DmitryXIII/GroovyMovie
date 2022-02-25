package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbActorDto
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDto
import retrofit2.Callback

interface IRetrofitRepository {
    fun getMoviesList(moviesListType: String, callback: Callback<TmdbResponse.ResponseMoviesList>)
    fun getGenresList(callback: Callback<TmdbResponse.ResponseGenres>)
    fun getMovieByIdWithCredits(movieId: Int, callback: Callback<TmdbMovieByIdDto>)
    fun getActorById(actorId: Int, callback: Callback<TmdbActorDto>)
}