package com.ineedyourcode.groovymovie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.App
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.db.IRoomRepository
import com.ineedyourcode.groovymovie.model.db.RoomRepository
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.IRetrofitRepository
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.RemoteDataSource
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.RetrofitRepository

class NoteViewModel(

): ViewModel() {

    private val roomRepository: IRoomRepository =
        RoomRepository(App.getMovieDao())

    fun saveNote(movie: Movie, noteContent: String) {
        roomRepository.saveNote(movie, noteContent)
    }

    fun getNote(movie: Movie): NotesEntity = roomRepository.getNote(movie.id)
}