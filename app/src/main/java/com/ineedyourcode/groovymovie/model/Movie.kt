package com.ineedyourcode.groovymovie.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: String?,
    val title: String?,
    val releaseDate: String?,
    val rating: String?,
    val genre: String?,
    val overview: String?
) : Parcelable