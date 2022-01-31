package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.ineedyourcode.groovymovie.model.tmdb.TMDBMovieDTO
import retrofit2.Callback

class RetrofitRepository(private val remoteDataSource: RemoteDataSource): IRetrofitRepository {

    override fun getTopRatedMovies(
        lang: String,
        page: Int
    ) {
        remoteDataSource.getTopRatedList(lang, page)
    }
}