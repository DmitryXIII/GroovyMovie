package com.ineedyourcode.groovymovie.model.tmdb.dto

import com.google.gson.annotations.SerializedName

/**
 * Модель фильма, получаемая из TMDB напрямую по id
 * (отличается от модели фильма, полуаемого в списке из TMDB)
 */
data class TmdbMovieByIdDTO (
        @SerializedName("adult") val adult: Boolean,
        @SerializedName("backdrop_path") val backdropPath: String,
        @SerializedName("genres") val genreIds: List<TmdbGenreDTO> = listOf(),
        @SerializedName("id") val id: Int,
        @SerializedName("original_language") val originalLanguage: String,
        @SerializedName("original_title") val originalTitle: String,
        @SerializedName("overview") val overview: String,
        @SerializedName("popularity") val popularity: Double,
        @SerializedName("poster_path") val posterPath: String,
        @SerializedName("release_date") val releaseDate: String,
        @SerializedName("title") val title:  String,
        @SerializedName("video") val video: Boolean,
        @SerializedName("vote_average") val voteAverage: Double,
        @SerializedName("vote_count") val voteCount: Int,
        @SerializedName("credits") val credit: TmdbCreditDto
    )