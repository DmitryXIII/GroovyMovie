package com.ineedyourcode.groovymovie.model.db

import com.ineedyourcode.groovymovie.model.Movie

interface IRoomHistoryRepository {
    fun getAllHistory(): List<ItemHistory>
    fun saveEntity(movie: Movie)
}