package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieByIdDTO
import retrofit2.Callback

class RetrofitRepository(private val remoteDataSource: RemoteDataSource) : IRetrofitRepository {

    override fun getTopRatedMovies(lang: String, page: Int, callback: Callback<TmdbResponse.ResponseMoviesList>) {
        remoteDataSource.getTopRatedList(lang, page, callback)
    }

    override fun getGenresList(lang: String, callback: Callback<TmdbResponse.ResponseGenres>) {
        remoteDataSource.getGenresList(lang, callback)
    }

    override fun getMovieById(id: Int, lang: String, callback: Callback<TmdbMovieByIdDTO>) {
        remoteDataSource.getMovieById(id, lang, callback)
    }
}