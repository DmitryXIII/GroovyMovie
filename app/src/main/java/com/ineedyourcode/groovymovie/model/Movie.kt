package com.ineedyourcode.groovymovie.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String?,
    val releaseDate: String?,
    val rating: String?,
    val genre: String?,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?
) : Parcelable