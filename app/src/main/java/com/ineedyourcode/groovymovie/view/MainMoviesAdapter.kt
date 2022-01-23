package com.ineedyourcode.groovymovie.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.Movie

class MainMoviesAdapter(
    private var moviesMap: Map<String, Movie>,
    private var genresList: List<String>,
    private var fragment: MainScreenFragment
) : RecyclerView.Adapter<MainMoviesAdapter.MainMoviesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainMoviesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.genre_item, parent, false)
        return MainMoviesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainMoviesViewHolder, position: Int) {
        // создание адаптера для каждого жанра из пришедшего списка фильмов
        val adapterByGenres = FilteredByGenresAdapter().apply {
            setAdapterData(moviesMap.values.toList().filter { it.genre == genresList[position] })

            // обработка клика по вложенным горизонтальным спискам фильмов
            setOnItemClickListener(object :
                FilteredByGenresAdapter.OnItemClickListener {
                override fun onItemClickListener(position: Int, filteredByGenresList: List<Movie>) {
                    fragment.parentFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(
                            R.id.fragment_container,
                            MovieInfoFragment.newInstance(filteredByGenresList[position])
                        )
                        .addToBackStack("")
                        .commit()
                }
            })
        }

        with(holder) {
            genreHeader.text = genresList[position]
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = adapterByGenres
            }
        }
    }

    override fun getItemCount() = genresList.size

    class MainMoviesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val genreHeader: TextView = itemView.findViewById(R.id.main_recycler_view_genre_header)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.movies_by_genres_recyclerview)
    }
}