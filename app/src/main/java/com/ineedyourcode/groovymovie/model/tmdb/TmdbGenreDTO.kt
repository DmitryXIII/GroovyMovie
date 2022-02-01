package com.ineedyourcode.groovymovie.model.tmdb

import com.google.gson.annotations.SerializedName

data class TmdbGenreDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)