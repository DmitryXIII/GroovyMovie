package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.ineedyourcode.groovymovie.BuildConfig
import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieByIdDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    @GET("movie/{listType}")
    fun getMoviesList(
        @Path("listType") moviesListType: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") lang: String = "ru-RU",
        @Query("page") page: Int = 1
    ): Call<TmdbResponse.ResponseMoviesList>

    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") lang: String = "ru-RU",
    ): Call<TmdbResponse.ResponseGenres>

    @GET("movie/{id}")
    fun getMovieByID(
        @Path("id") id: Int = 14,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") lang: String = "ru-RU",
    ): Call<TmdbMovieByIdDTO>
}