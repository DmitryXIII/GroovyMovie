package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.google.gson.annotations.SerializedName
import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieDTO
import com.ineedyourcode.groovymovie.model.tmdb.TmdbGenreDTO

sealed class TmdbResponse {
    class ResponseMoviesList(
        @SerializedName("page") val page: Int,
        @SerializedName("results") val movies: List<TmdbMovieDTO>,
        @SerializedName("total_pages") val pages: Int
    )

    class ResponseGenres(
        @SerializedName("genres") val genres: List<TmdbGenreDTO>
    )

//    class ResponseMovie(
//       @("") val movie: TmdbMovieDTO
//    )
}
