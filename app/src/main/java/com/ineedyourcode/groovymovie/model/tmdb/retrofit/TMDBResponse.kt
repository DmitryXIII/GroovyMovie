package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.google.gson.annotations.SerializedName
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.tmdb.TMDBMovieDTO

data class TMDBResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: List<TMDBMovieDTO>,
    @SerializedName("total_pages") val pages: Int
)