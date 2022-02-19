package com.ineedyourcode.groovymovie.view.note

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentNoteBinding
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDto
import com.ineedyourcode.groovymovie.utils.hideKeyboard
import com.ineedyourcode.groovymovie.utils.showSnackWithoutAction
import com.ineedyourcode.groovymovie.view.BaseBindingFragment
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.NoteViewModel

class NoteFragment : BaseBindingFragment<FragmentNoteBinding>(FragmentNoteBinding::inflate) {

    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }

    private lateinit var selectedMovie: TmdbMovieByIdDto

    companion object {
        private const val ARG_MOVIE = "ARG_MOVIE"
        fun newInstance(movie: TmdbMovieByIdDto) = NoteFragment().apply {
            arguments = bundleOf(
                ARG_MOVIE to movie
            )
            selectedMovie = movie
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noteMovieTitle.text = getString(R.string.note_content_title, selectedMovie.title)

        noteViewModel.getNote(selectedMovie).observe(viewLifecycleOwner, Observer<Any> {
            when (it) {
                is AppState.NoteSuccess -> {
                    binding.noteContent.setText(it.note.noteContent)
                }
            }
        })

        binding.fabSaveNote.setOnClickListener {
            view.hideKeyboard()
            noteViewModel.saveNote(selectedMovie, binding.noteContent.text.toString())
            view.showSnackWithoutAction("Заметка сохранена")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // если содержание заметки при выходе пустое, то заметка удаляется из БД
        if (binding.noteContent.text.isNullOrBlank()) {
            noteViewModel.deleteNote(selectedMovie)
        }
    }
}