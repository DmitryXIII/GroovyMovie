package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.ineedyourcode.groovymovie.BuildConfig
import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {
    @GET("movie/top_rated")
    fun getTopRated(
        @Query("api_key") token: String = BuildConfig.TMDB_API_KEY,
        @Query("language") lang: String,
        @Query("page") page: Int
    ): Call<TmdbResponse.ResponseMoviesList>

    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") token: String = BuildConfig.TMDB_API_KEY,
        @Query("language") lang: String,
    ): Call<TmdbResponse.ResponseGenres>

    @GET("movie/{id}")
    fun getMovie(
        @Path("id") id: Int,
        @Query("api_key") token: String = BuildConfig.TMDB_API_KEY,
        @Query("language") lang: String,
    ): Call<TmdbMovieDTO>
}