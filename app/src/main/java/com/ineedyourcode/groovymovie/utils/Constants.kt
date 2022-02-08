package com.ineedyourcode.groovymovie.utils

const val MOVIES_LIST_TOP_RATED = "top_rated"
const val MOVIES_LIST_NOW_PLAYING = "now_playing"
const val MOVIES_LIST_POPULAR = "popular"
const val MOVIES_LIST_UPCOMING = "upcoming"

const val PREFERENCES_ADULT = "FILTER ADULT"
const val FRAGMENT_RESULT_KEY = "Settings fragment"

var favoriteMap: MutableMap<Int, Boolean> = mutableMapOf()