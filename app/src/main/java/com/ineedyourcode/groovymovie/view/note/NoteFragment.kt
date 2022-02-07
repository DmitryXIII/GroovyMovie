package com.ineedyourcode.groovymovie.view.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentNoteBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.utils.hideKeyboard
import com.ineedyourcode.groovymovie.utils.showSnackWithoutAction
import com.ineedyourcode.groovymovie.viewmodel.NoteViewModel

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }

    private lateinit var selectedMovie: Movie

    companion object {
        private const val ARG_MOVIE = "ARG_MOVIE"
        fun newInstance(movie: Movie) = NoteFragment().apply {
            arguments = bundleOf(
                ARG_MOVIE to movie
            )
            selectedMovie = movie
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noteMovieTitle.text = getString(R.string.note_content_title, selectedMovie.title)

        // если заметка еще не была создана, то создаем в БД заметку с пустым содержанием,
        // иначе NullPointerException
        try {
            binding.noteContent.setText(noteViewModel.getNote(selectedMovie).noteContent)
        } catch (e: NullPointerException) {
            noteViewModel.saveNote(selectedMovie, "")
            binding.noteContent.setText("")
        }

        binding.fabSaveNote.setOnClickListener {
            view.hideKeyboard()
            noteViewModel.saveNote(selectedMovie, binding.noteContent.text.toString())
            binding.noteContent.setText(noteViewModel.getNote(selectedMovie).noteContent)
            view.showSnackWithoutAction("Заметка сохранена")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // если содержание заметки при выходе пустое, то заметка удаляется из БД
        if (binding.noteContent.text.isNullOrBlank()) {
            noteViewModel.deleteNote(selectedMovie)
        }

        _binding = null
    }
}