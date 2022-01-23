package com.ineedyourcode.groovymovie.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMainScreenBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.MainScreenViewModel

class MainScreenFragment : Fragment() {

    private lateinit var adapter: MovieListAdapter
    private lateinit var moviesList: List<Movie>
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressCircular: ProgressBar
    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner, Observer<Any> {
            renderData(it as AppState)
        })

        adapter = MovieListAdapter()

        val searchLayout = binding.tfInputSearch
        val searchValue = binding.tfEditSearch
        progressCircular = binding.progressCircular

        recyclerView = binding.moviesRecyclerview
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

        searchLayout.setEndIconOnClickListener(View.OnClickListener {
            if ((searchValue.text.toString().isBlank())) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_request),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Поиск \"${searchValue.text}\"",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            val imm: InputMethodManager =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        })
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                progressCircular.isVisible = false
                recyclerView.isVisible = true
                moviesList = appState.moviesData
                adapter.setAdapterData(moviesList)
            }
            is AppState.Loading -> {
                progressCircular.isVisible = true
                recyclerView.visibility = View.INVISIBLE
            }
            is AppState.Error -> {
                progressCircular.isVisible = false
                recyclerView.visibility = View.INVISIBLE
                Snackbar
                    .make(binding.tfInputSearch, appState.error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.retry)) { viewModel.getData() }
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}