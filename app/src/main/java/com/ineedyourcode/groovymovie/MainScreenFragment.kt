package com.ineedyourcode.groovymovie

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout

class MainScreenFragment : Fragment() {

    private val repository = RandomMoviesRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.movies_recyclerview)
        val moviesList = repository.getMoviesList()
        val adapter = MovieListAdapter(moviesList)
        val searcher: TextInputLayout = view.findViewById(R.id.tf_input_search)
        val lookingFor: EditText = view.findViewById(R.id.tf_edit_search)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : MovieListAdapter.OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                val selectedMovie = moviesList[position]
                parentFragmentManager.beginTransaction()
                    .setTransition(TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, MovieInfoFragment.newInstance(selectedMovie))
                    .addToBackStack("")
                    .commit()
            }
        })

        searcher.setEndIconOnClickListener(View.OnClickListener {
            if ((lookingFor.text.toString().isBlank())) {
                Toast.makeText(requireContext(), "Пустой запрос", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Поиск \"${lookingFor.text}\"", Toast.LENGTH_LONG)
                    .show()
            }

            val imm: InputMethodManager =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        })
    }
}