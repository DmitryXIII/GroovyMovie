package com.ineedyourcode.groovymovie.view.movieslist

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMoviesListBinding
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO
import com.ineedyourcode.groovymovie.utils.PREFERENCES_ADULT
import com.ineedyourcode.groovymovie.utils.showSnackWithAction
import com.ineedyourcode.groovymovie.utils.GridDecorator
import com.ineedyourcode.groovymovie.utils.convertDpToPixels
import com.ineedyourcode.groovymovie.view.details.MovieDetailsFragment
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.MoviesListViewModel

@RequiresApi(Build.VERSION_CODES.N)
class MoviesListFragment : Fragment() {

    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var mainAdapter: MoviesListAdapter
    private lateinit var moviesListType: String
    private lateinit var progressBar: ProgressBar // кастомный прогрессбар

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesListViewModel by lazy {
        ViewModelProvider(this)[MoviesListViewModel::class.java]
    }

    companion object {
        private const val ARG_MOVIE_TYPE = "ARG_MOVIE_TYPE"

        fun newInstance(moviesListType: String) =
            MoviesListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MOVIE_TYPE, moviesListType)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            moviesListType = it.getString(ARG_MOVIE_TYPE)!!
        }

        viewModel.getMoviesList(moviesListType).observe(viewLifecycleOwner, Observer<Any> {
            renderData(it as AppState)
        })

        with(binding) {
            progressBar = spinKit
            mainRecyclerView = binding.mainRecyclerview
        }

        progressBar.indeterminateDrawable = ThreeBounce()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.MoviesListSuccess -> {
                progressBar.isVisible = false
                mainRecyclerView.visibility = View.VISIBLE

                mainAdapter = MoviesListAdapter()

                activity?.let {
                    if (it.getPreferences(Context.MODE_PRIVATE)
                            .getBoolean(PREFERENCES_ADULT, false)
                    ) {
                        mainAdapter.setAdapterData(appState.moviesData.filter {movie -> !movie.adult })
                    } else {
                        mainAdapter.setAdapterData(appState.moviesData)
                    }
                }

                mainAdapter.setOnItemClickListener(object :
                    MoviesListAdapter.OnItemClickListener {
                    override fun onItemClickListener(
                        position: Int,
                        moviesList: List<TmdbMovieByIdDTO>
                    ) {
                        parentFragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(
                                R.id.main_fragment_container,
                                MovieDetailsFragment.newInstance(moviesList[position])
                            )
                            .addToBackStack("")
                            .commit()

                        viewModel.saveHistory(moviesList[position])
                    }
                })

                mainRecyclerView.apply {
                    layoutManager = GridLayoutManager(requireContext(), 2)
                    adapter = mainAdapter
                    addItemDecoration(
                        GridDecorator(
                            2,
                            convertDpToPixels(resources, R.dimen.movie_item_width).toFloat()
                        )
                    )
                }
            }

            is AppState.Loading -> {
                progressBar.isVisible = true
                mainRecyclerView.visibility = View.INVISIBLE
            }

            is AppState.Error -> {
                progressBar.isVisible = false
                mainRecyclerView.visibility = View.INVISIBLE

                view?.showSnackWithAction(
                    appState.e,
                    getString(R.string.retry)
                ) {
                    viewModel.getMoviesList(moviesListType)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}