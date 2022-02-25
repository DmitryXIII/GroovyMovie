package com.ineedyourcode.groovymovie.model.tmdb.dto

import com.google.gson.annotations.SerializedName

data class TmdbCastDto(
    @SerializedName("id") val id: Int,
    @SerializedName("known_for_department") val department: String,
    @SerializedName("name") val name: String
)