package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieByIdDTO
import retrofit2.Callback

class RetrofitRepository(private val remoteDataSource: RemoteDataSource) : IRetrofitRepository {

    override fun getMoviesList(moviesListType: String, callback: Callback<TmdbResponse.ResponseMoviesList>) {
        remoteDataSource.getMoviesList(moviesListType, callback)
    }

    override fun getGenresList(callback: Callback<TmdbResponse.ResponseGenres>) {
        remoteDataSource.getGenresList(callback)
    }

    override fun getMovieById(callback: Callback<TmdbMovieByIdDTO>) {
        remoteDataSource.getMovieById(callback)
    }
}