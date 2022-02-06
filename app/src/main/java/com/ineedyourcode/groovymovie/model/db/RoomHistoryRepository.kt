package com.ineedyourcode.groovymovie.model.db

import android.util.Log
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.utils.convertHistoryEntityToMovie
import com.ineedyourcode.groovymovie.utils.convertMovieToHistoryEntity

class RoomHistoryRepository(private val roomDataSource: HistoryDAO) : IRoomHistoryRepository {
    override fun getAllHistory(): List<ItemHistory> = convertHistoryEntityToMovie(roomDataSource.all())

    override fun saveEntity(movie: Movie) {
        roomDataSource.insert(convertMovieToHistoryEntity(movie))
        var l: List<ItemHistory> = getAllHistory()
        Log.d("ROOM_DB_HISTORY", l.toString())
    }
}