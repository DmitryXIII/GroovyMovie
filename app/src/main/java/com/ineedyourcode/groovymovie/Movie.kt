package com.ineedyourcode.groovymovie

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val title: String?,
    val releaseDate: String?,
    val rating: String?,
    val genre: String?,

    @DrawableRes
    val poster: Int

) : Parcelable