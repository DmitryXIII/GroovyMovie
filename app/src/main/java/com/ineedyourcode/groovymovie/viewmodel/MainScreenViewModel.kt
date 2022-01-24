package com.ineedyourcode.groovymovie.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.model.IMoviesRepository
import com.ineedyourcode.groovymovie.model.tmdb.TMDBRepository
import java.lang.Thread.sleep

@RequiresApi(Build.VERSION_CODES.N)
class MainScreenViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {

    private val repository: IMoviesRepository = TMDBRepository()

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
                in 8..10 -> liveDataToObserve.postValue(AppState.Error(IllegalAccessException("Data receiving error")))
            }
        }.start()
    }

    private fun randomResult(): Int {
        return (1..10).random()
    }
}