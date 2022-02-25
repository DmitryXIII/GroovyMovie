package com.ineedyourcode.groovymovie.model.db

import androidx.room.*
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity

@Dao
interface MovieDao {
    // HISTORY
    @Query("SELECT * FROM HistoryEntity")
    fun getAllHistory(): List<HistoryEntity>

    @Query("DELETE FROM HistoryEntity")
    fun clearAllHistory()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistoryEntity(entity: HistoryEntity)

    @Update
    fun updateHistoryEntity(entity: HistoryEntity)

    // FAVORITE
    @Query("SELECT * FROM FavoriteEntity")
    fun getAllFavorite(): List<FavoriteEntity>

    @Query("DELETE FROM FavoriteEntity")
    fun clearAllFavorite()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteEntity(entity: FavoriteEntity)

    @Query("DELETE FROM FavoriteEntity WHERE movieId == :movieId")
    fun deleteFavorite(movieId: Int)

    @Query("SELECT * FROM FavoriteEntity WHERE movieId == :movieId")
    fun getFavorite(movieId: Int): FavoriteEntity

    // NOTES
    @Query("SELECT * FROM NotesEntity WHERE movieId == :id")
    fun getNote(id: Int): NotesEntity

    @Query("DELETE FROM NotesEntity WHERE movieId == :movieId")
    fun deleteNote(movieId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(entity: NotesEntity)
}