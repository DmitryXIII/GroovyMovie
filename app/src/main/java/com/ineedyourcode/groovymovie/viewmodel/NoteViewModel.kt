package com.ineedyourcode.groovymovie.viewmodel

import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.App
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.db.IRoomRepository
import com.ineedyourcode.groovymovie.model.db.RoomRepository
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity

class NoteViewModel: ViewModel() {

    private val roomNoteRepository: IRoomRepository =
        RoomRepository(App.getMovieDao())

    fun saveNote(movie: Movie, noteContent: String) {
        roomNoteRepository.saveNote(movie, noteContent)
    }

    fun getNote(movie: Movie): NotesEntity = roomNoteRepository.getNote(movie.id)

    fun deleteNote(movie: Movie){
        roomNoteRepository.deleteNote(movie.id)
    }
}