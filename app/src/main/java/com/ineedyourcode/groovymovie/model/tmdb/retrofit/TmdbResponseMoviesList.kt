package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.google.gson.annotations.SerializedName
import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieDTO

data class TmdbResponseMoviesList(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: List<TmdbMovieDTO>,
    @SerializedName("total_pages") val pages: Int
)