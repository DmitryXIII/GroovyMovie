package com.ineedyourcode.groovymovie.model.tmdb

import com.google.gson.annotations.SerializedName


/**
 * Модель жанра, получаемая в общем списке жанров из TMDB.
 * Если запрашивается список фильмов, то в списке каждый фильм имеет поле "жанр", где жанры прописаны
 * в виде id. Чтобы получить названия жанров, надо сопоставть id жанра в фильме с id жанра из общего списка жанров
 */
data class TmdbGenreDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)