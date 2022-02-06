package com.ineedyourcode.groovymovie.model.db

import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.utils.convertHistoryEntityToMovie
import com.ineedyourcode.groovymovie.utils.convertMovieToHistoryEntity

class RoomHistoryRepository(private val roomDataSource: HistoryDAO) : IRoomHistoryRepository {
    override fun getAllHistory(): List<Int> = convertHistoryEntityToMovie(roomDataSource.all())

    override fun saveEntity(movie: Movie) {
        roomDataSource.insert(convertMovieToHistoryEntity(movie))
    }
}