package com.ineedyourcode.groovymovie.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Movie(
    val id: String?,
    val title: String?,
    val releaseDate: String?,
    val rating: String?,
    val genre: String?,

    @DrawableRes
    val poster: Int

) : Parcelable