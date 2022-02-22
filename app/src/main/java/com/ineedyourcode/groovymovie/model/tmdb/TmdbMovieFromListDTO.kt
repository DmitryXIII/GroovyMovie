package com.ineedyourcode.groovymovie.model.tmdb

import com.google.gson.annotations.SerializedName

/**
 * Модель фильма, получаемая из списка фильмов TMDB
 * (отличается от модели фильма, полуаемого напрямую по id)
 */
data class TmdbMovieFromListDTO(
    @SerializedName ("adult") val adult: Boolean,
    @SerializedName ("backdrop_path") val backdropPath: String,
    @SerializedName ("genre_ids") val genreIds: IntArray,
    @SerializedName ("id") val id: Int,
    @SerializedName ("original_language") val originalLanguage: String,
    @SerializedName ("original_title") val originalTitle: String,
    @SerializedName ("overview") val overview: String,
    @SerializedName ("popularity") val popularity: Double,
    @SerializedName ("poster_path") val posterPath: String,
    @SerializedName ("release_date") val releaseDate: String,
    @SerializedName ("title") val title:  String,
    @SerializedName ("video") val video: Boolean,
    @SerializedName ("vote_average") val voteAverage: Double,
    @SerializedName ("vote_count") val voteCount: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TmdbMovieFromListDTO

        if (!genreIds.contentEquals(other.genreIds)) return false

        return true
    }

    override fun hashCode(): Int {
        return genreIds.contentHashCode()
    }
}