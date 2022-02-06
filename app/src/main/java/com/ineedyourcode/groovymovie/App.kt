package com.ineedyourcode.groovymovie

import android.app.Application
import androidx.room.Room
import com.ineedyourcode.groovymovie.model.db.HistoryDAO
import com.ineedyourcode.groovymovie.model.db.HistoryDataBase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: App? = null
        private var historyDb: HistoryDataBase? = null
        private const val HISTORY_DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDAO{
            if (historyDb == null){
                synchronized(HistoryDataBase::class.java){
                    if (historyDb == null){
                        if (instance == null) throw IllegalAccessException("App is null")
                        historyDb = Room.databaseBuilder(
                            instance!!.applicationContext,
                            HistoryDataBase::class.java,
                            HISTORY_DB_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }

            return historyDb!!.historyDao()
        }
    }

}