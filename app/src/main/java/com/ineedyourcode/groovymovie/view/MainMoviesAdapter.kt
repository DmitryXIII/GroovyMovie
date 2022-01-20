package com.ineedyourcode.groovymovie.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.Movie

class MainMoviesAdapter : RecyclerView.Adapter<MainMoviesAdapter.MainMoviesViewHolder>() {

    private var genresList: List<String> = listOf()
    private var moviesMap: Map<String, Movie> = mapOf()
    private lateinit var context: Context
    private lateinit var selectedMovie: Movie
    private lateinit var fragment: MainScreenFragment

    fun setAdapterData(
        moviesMap: Map<String, Movie>,
        genresList: List<String>,
        context: Context,
        fragment: MainScreenFragment
    ) {

        notifyDataSetChanged()
        this.moviesMap = moviesMap
        this.genresList = genresList
        this.context = context
        this.fragment = fragment
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainMoviesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.genre_item, parent, false)
        return MainMoviesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainMoviesViewHolder, position: Int) {
        val adapterByGenres = FilteredByGenresAdapter()
        val list = moviesMap.values.toList().filter { it.genre == genresList[position] }
        adapterByGenres.setAdapterData(list)

        with(holder) {
            genreHeader.text = genresList[position]
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapterByGenres
        }

        adapterByGenres.setOnItemClickListener(object :
            FilteredByGenresAdapter.OnItemClickListener {
            override fun onItemClickListener(position: Int, filteredByGenresList: List<Movie>) {
                selectedMovie = filteredByGenresList[position]
                fragment.parentFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, MovieInfoFragment.newInstance(selectedMovie))
                    .addToBackStack("")
                    .commit()
            }
        })
    }

    override fun getItemCount() = genresList.size

    class MainMoviesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val genreHeader: TextView = itemView.findViewById(R.id.main_recycler_view_genre_header)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.movies_by_genres_recyclerview)
    }
}