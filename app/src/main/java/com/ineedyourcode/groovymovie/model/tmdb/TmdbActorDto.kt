package com.ineedyourcode.groovymovie.model.tmdb

import com.google.gson.annotations.SerializedName

data class TmdbActorDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("place_of_birth") val birthPlace: String
)