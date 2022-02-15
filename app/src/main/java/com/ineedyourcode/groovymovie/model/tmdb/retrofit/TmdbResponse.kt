package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import com.google.gson.annotations.SerializedName
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbGenreDTO
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO

sealed class TmdbResponse {

    // для callback при запросе списка фильмов
    class ResponseMoviesList(
        @SerializedName("page") val page: Int,
        @SerializedName("results") val movieFromLists: List<TmdbMovieByIdDTO>,
        @SerializedName("total_pages") val pages: Int
    )

    // для callback при запросе списка жанров
    class ResponseGenres(
        @SerializedName("genres") val genres: List<TmdbGenreDTO>
    )
}
