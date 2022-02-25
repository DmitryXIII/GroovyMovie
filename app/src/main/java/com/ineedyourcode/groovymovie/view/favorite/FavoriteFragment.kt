package com.ineedyourcode.groovymovie.view.favorite

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentFavoriteBinding
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDto
import com.ineedyourcode.groovymovie.utils.GridDecorator
import com.ineedyourcode.groovymovie.utils.showSnackWithoutAction
import com.ineedyourcode.groovymovie.view.BaseBindingFragment
import com.ineedyourcode.groovymovie.view.details.MovieDetailsFragment
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.FavoriteViewModel

class FavoriteFragment :
    BaseBindingFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    val TAG_FOR_BACKSTACK = "FavoriteFragment"
    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var progressBar: ProgressBar // кастомный прогрессбар

    private val viewModel: FavoriteViewModel by lazy {
        ViewModelProvider(this)[FavoriteViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAdapter = FavoriteAdapter()

        viewModel.getAllFavorite().observe(viewLifecycleOwner) {
            when (it) {
                is AppState.Loading -> {
                    binding.favoriteSpinKit.isVisible = true
                }

                is AppState.Success -> {
                    favoriteAdapter.clearData()
                    binding.noFavorite.isVisible = true
                    binding.favoriteRecyclerview.isVisible = false
                }

                is AppState.FavoriteListSuccess -> {
                    if (it.favoriteList.isEmpty()) {
                        binding.noFavorite.isVisible = true
                        binding.favoriteRecyclerview.isVisible = false
                    } else {
                        favoriteAdapter.setAdapterData(it.favoriteList)
                        binding.noFavorite.isVisible = false
                        binding.favoriteRecyclerview.isVisible = true
                    }
                    binding.favoriteSpinKit.isVisible = false
                }

                is AppState.Error -> {
                    view.showSnackWithoutAction(R.string.data_receiving_error)
                }
                else -> {}
            }
        }

        progressBar = binding.favoriteSpinKit
        favoriteRecyclerView = binding.favoriteRecyclerview

        progressBar.indeterminateDrawable = ThreeBounce()

        favoriteRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favoriteAdapter
            addItemDecoration(
                GridDecorator(
                    2,
                    resources.getDimensionPixelSize(R.dimen.movie_item_width).toFloat()
                )
            )
        }

        favoriteAdapter.setOnItemClickListener(object :
            FavoriteAdapter.OnItemClickListener {
            override fun onItemClickListener(
                position: Int,
                moviesList: List<TmdbMovieByIdDto>
            ) {
                parentFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(
                        R.id.main_fragment_container,
                        MovieDetailsFragment.newInstance(moviesList[position])
                    )
                    .addToBackStack("")
                    .commit()
            }
        })

        binding.fabClearFavorite.setOnClickListener {
            viewModel.deleteAllFavorite()
        }
    }
}