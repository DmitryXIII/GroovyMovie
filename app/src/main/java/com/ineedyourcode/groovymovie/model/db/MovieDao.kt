package com.ineedyourcode.groovymovie.model.db

import androidx.room.*
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM HistoryEntity")
    fun getAllHistory(): List<HistoryEntity>

    @Query("DELETE FROM HistoryEntity")
    fun clearAllHistory()

    @Query("SELECT * FROM NotesEntity WHERE movieId == :id")
    fun getNote(id: Int): NotesEntity

    @Query("DELETE FROM NotesEntity WHERE movieId == :movieId")
    fun deleteNote(movieId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistoryEntity(entity: HistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(entity: NotesEntity)

    @Update
    fun updateHistoryEntity(entity: HistoryEntity)

}