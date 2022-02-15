package com.ineedyourcode.groovymovie.model.db

import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO
import com.ineedyourcode.groovymovie.utils.convertMovieToHistoryEntity
import com.ineedyourcode.groovymovie.utils.convertMovieToNoteEntity

class RoomRepository(private val roomDataSource: MovieDao) : IRoomRepository {
    // HISTORY
    override fun getAllHistory(): List<HistoryEntity> = roomDataSource.getAllHistory()

    override fun saveHistoryEntity(movie: TmdbMovieByIdDTO) {
        roomDataSource.insertHistoryEntity(convertMovieToHistoryEntity(movie))
    }

    override fun clearAllHistory() {
        roomDataSource.clearAllHistory()
    }

    // FAVORITE
    override fun getAllFavorite(): List<FavoriteEntity> = roomDataSource.getAllFavorite()

    override fun saveFavoriteEntity(entity: FavoriteEntity) {
        roomDataSource.insertFavoriteEntity(entity)
    }

    override fun clearAllFavorite() {
        roomDataSource.clearAllFavorite()
    }

    override fun deleteFavorite(movieId: Int) {
        roomDataSource.deleteFavorite(movieId)
    }

    // NOTE
    override fun saveNote(movie: TmdbMovieByIdDTO, noteContent: String) {
        roomDataSource.insertNote(convertMovieToNoteEntity(movie, noteContent))
    }

    override fun getNote(movieId: Int): NotesEntity = roomDataSource.getNote(movieId)

    override fun deleteNote(movieId: Int) {
        roomDataSource.deleteNote(movieId)
    }
}