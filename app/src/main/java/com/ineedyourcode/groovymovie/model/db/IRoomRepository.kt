package com.ineedyourcode.groovymovie.model.db

import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity

interface IRoomRepository {
    // HISTORY
    fun getAllHistory(): List<HistoryEntity>
    fun saveHistoryEntity(movie: Movie)
    fun clearAllHistory()

    // FAVORITE
    fun getAllFavorite(): List<FavoriteEntity>
    fun saveFavoriteEntity(entity: FavoriteEntity)
    fun clearAllFavorite()
    fun deleteFavorite(movieId: Int)

    // NOTE
    fun saveNote(movie: Movie, noteContent: String)
    fun getNote(movieId: Int): NotesEntity
    fun deleteNote(movieId: Int)
}