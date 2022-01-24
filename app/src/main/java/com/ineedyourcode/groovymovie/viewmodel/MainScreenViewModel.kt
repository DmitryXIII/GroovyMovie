package com.ineedyourcode.groovymovie.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.model.RandomMoviesRepository
import com.ineedyourcode.groovymovie.model.TMDBRepository
import java.lang.Thread.sleep

class MainScreenViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {

    private val repository = RandomMoviesRepository()
private val rep = TMDBRepository()

    @RequiresApi(Build.VERSION_CODES.N)
    fun getData(): LiveData<AppState> {
        getDataFromDb()
        rep.loadWeather()
        return liveDataToObserve
    }

    private fun getDataFromDb() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            when (randomResult()) {
                in 1..7 ->
                    liveDataToObserve.postValue(
                        AppState.Success(
                            repository.getMoviesMap(),
                            repository.getGenresList()
                        )
                    )
                in 8..10 -> liveDataToObserve.postValue(AppState.Error(IllegalAccessException("Data receiving error")))
            }
        }.start()
    }

    private fun randomResult(): Int {
        return (1..10).random()
    }
}