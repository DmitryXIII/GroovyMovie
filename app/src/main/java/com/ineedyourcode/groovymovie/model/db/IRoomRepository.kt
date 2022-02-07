package com.ineedyourcode.groovymovie.model.db

import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity

interface IRoomRepository {
    fun getAllHistory(): List<HistoryEntity>
    fun saveHistoryEntity(movie: Movie)
    fun clearAllHistory()
    fun saveNote(movie: Movie, noteContent: String)
    fun getNote(movieId: Int): NotesEntity
    fun deleteNote(movieId: Int)
}