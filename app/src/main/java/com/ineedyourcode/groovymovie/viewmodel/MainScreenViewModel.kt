package com.ineedyourcode.groovymovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.model.RandomMoviesRepository
import java.lang.Thread.sleep

class MainScreenViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {

    private val repository = RandomMoviesRepository()

    fun getData(): LiveData<AppState> {
        getDataFromDb()
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
                in 8..10 -> liveDataToObserve.postValue(AppState.Error("Data receiving error"))
            }
        }.start()
    }

    private fun randomResult(): Int {
        return (1..10).random()
    }
}