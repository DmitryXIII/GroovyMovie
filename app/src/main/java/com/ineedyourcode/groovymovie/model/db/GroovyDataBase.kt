package com.ineedyourcode.groovymovie.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity

@Database(entities = [HistoryEntity::class, NotesEntity::class, FavoriteEntity::class], version = 1, exportSchema = false)
abstract class GroovyDataBase : RoomDatabase() {
    abstract fun movieDao() : MovieDao
}
