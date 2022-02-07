package com.ineedyourcode.groovymovie.model.db

import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import com.ineedyourcode.groovymovie.utils.convertMovieToHistoryEntity
import com.ineedyourcode.groovymovie.utils.convertMovieToNoteEntity

class RoomRepository(private val roomDataSource: MovieDao) : IRoomRepository {
    override fun getAllHistory(): List<HistoryEntity> = roomDataSource.getAllHistory()

    override fun saveHistoryEntity(movie: Movie) {
        roomDataSource.insertHistoryEntity(convertMovieToHistoryEntity(movie))
    }

    override fun clearAllHistory() {
        roomDataSource.clearAllHistory()
    }

    override fun saveNote(movie: Movie, noteContent: String) {
        roomDataSource.insertNote(convertMovieToNoteEntity(movie, noteContent))
    }

    override fun getNote(movieId: Int): NotesEntity = roomDataSource.getNote(movieId)

    override fun deleteNote(movieId: Int) {
        roomDataSource.deleteNote(movieId)
    }
}