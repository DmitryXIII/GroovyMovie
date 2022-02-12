package com.ineedyourcode.groovymovie.model.tmdb.dto

import com.google.gson.annotations.SerializedName

data class TmdbCreditDto(
    @SerializedName("cast") val cast: List<TmdbCastDto>
)