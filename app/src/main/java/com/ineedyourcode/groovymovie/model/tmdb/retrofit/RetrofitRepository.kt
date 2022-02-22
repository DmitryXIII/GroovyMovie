package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbActorDto
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO
import retrofit2.Callback

class RetrofitRepository(private val remoteDataSource: RemoteDataSource) : IRetrofitRepository {

    override fun getMoviesList(moviesListType: String, callback: Callback<TmdbResponse.ResponseMoviesList>) {
        remoteDataSource.getMoviesList(moviesListType, callback)
    }

    override fun getGenresList(callback: Callback<TmdbResponse.ResponseGenres>) {
        remoteDataSource.getGenresList(callback)
    }

    override fun getMovieByIdWithCredits(movieId: Int, callback: Callback<TmdbMovieByIdDTO>) {
        remoteDataSource.getMovieByIdWithCredits(movieId, callback)
    }

    override fun getActorById(actorId: Int, callback: Callback<TmdbActorDto>) {
        remoteDataSource.getActorById(actorId, callback)
    }
}