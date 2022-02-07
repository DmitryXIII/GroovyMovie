package com.ineedyourcode.groovymovie.view.history

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentHistoryBinding
import com.ineedyourcode.groovymovie.viewmodel.ViewModelRetrofit

class HistoryFragment : Fragment() {

    private lateinit var historyAdapter: HistoryAdapter

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModelHistory: ViewModelRetrofit by lazy {
        ViewModelProvider(this)[ViewModelRetrofit::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyAdapter = HistoryAdapter()
        historyAdapter.setAdapterData(viewModelHistory.getHistory())

        binding.historyRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }

        // Замена цвета иконки внутри FAB
        ImageViewCompat.setImageTintList(
            binding.fabClearHistory,
            ColorStateList.valueOf(resources.getColor(R.color.main_background, context?.theme))
        )

        binding.fabClearHistory.setOnClickListener {
            viewModelHistory.clearHistory()
            historyAdapter.clearData()
            Toast.makeText(requireContext(), "HISTORY CLEARED", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}