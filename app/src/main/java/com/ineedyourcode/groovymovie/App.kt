package com.ineedyourcode.groovymovie

import android.app.Application
import androidx.room.Room
import com.ineedyourcode.groovymovie.model.db.GroovyDataBase
import com.ineedyourcode.groovymovie.model.db.MovieDao

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: App? = null
        private var appDb: GroovyDataBase? = null
        private const val APP_DB_NAME = "Groovy.db"

        fun getMovieDao(): MovieDao {
            if (appDb == null){
                synchronized(GroovyDataBase::class.java){
                    if (appDb == null){
                        if (instance == null) throw IllegalAccessException("App is null")
                        appDb = Room.databaseBuilder(
                            instance!!.applicationContext,
                            GroovyDataBase::class.java,
                            APP_DB_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }

            return appDb!!.movieDao()
        }
    }

}