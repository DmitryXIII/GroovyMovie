package com.ineedyourcode.groovymovie.model.db

import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO

interface IRoomRepository {
    // HISTORY
    fun getAllHistory(): List<HistoryEntity>
    fun saveHistoryEntity(movie: TmdbMovieByIdDTO)
    fun clearAllHistory()

    // FAVORITE
    fun getAllFavorite(): List<FavoriteEntity>
    fun saveFavoriteEntity(entity: FavoriteEntity)
    fun clearAllFavorite()
    fun deleteFavorite(movieId: Int)
    fun checkIsFavorite(movieId: Int): Boolean

    // NOTE
    fun saveNote(movie: TmdbMovieByIdDTO, noteContent: String)
    fun getNote(movieId: Int): NotesEntity?
    fun deleteNote(movieId: Int)
}