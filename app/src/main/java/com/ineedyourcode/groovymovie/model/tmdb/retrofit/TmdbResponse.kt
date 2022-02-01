package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.google.gson.annotations.SerializedName
import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieFromListDTO
import com.ineedyourcode.groovymovie.model.tmdb.TmdbGenreDTO

sealed class TmdbResponse {

    // для callback при запросе списка фильмов
    class ResponseMoviesList(
        @SerializedName("page") val page: Int,
        @SerializedName("results") val movieFromLists: List<TmdbMovieFromListDTO>,
        @SerializedName("total_pages") val pages: Int
    )

    // для callback при запросе списка жанров
    class ResponseGenres(
        @SerializedName("genres") val genres: List<TmdbGenreDTO>
    )
}
