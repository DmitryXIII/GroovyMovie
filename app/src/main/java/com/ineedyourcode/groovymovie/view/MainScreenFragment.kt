package com.ineedyourcode.groovymovie.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.MovieListAdapter
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMainScreenBinding
import com.ineedyourcode.groovymovie.model.RandomMoviesRepository
import com.ineedyourcode.groovymovie.viewmodel.MainScreenViewModel

class MainScreenFragment : Fragment() {

    companion object {
        fun newInstance() = MainScreenFragment()
    }

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private val repository = RandomMoviesRepository()

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

        val recyclerView: RecyclerView = view.findViewById(R.id.movies_recyclerview)
        val moviesList = repository.getMoviesList()
        val adapter = MovieListAdapter(moviesList)
        val searchLayout = binding.tfInputSearch
        val searchValue = binding.tfEditSearch

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
                Toast.makeText(requireContext(), "Пустой запрос", Toast.LENGTH_LONG).show()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner, Observer<Any> {
            renderData(it)
        })
    }

    private fun renderData(it: Any?) {
        Toast.makeText(requireContext(), "get data", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}