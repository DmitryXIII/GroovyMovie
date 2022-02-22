package com.ineedyourcode.groovymovie.view.history

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentHistoryBinding
import com.ineedyourcode.groovymovie.utils.showSnackWithoutAction
import com.ineedyourcode.groovymovie.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private lateinit var historyAdapter: HistoryAdapter

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModelHistory: HistoryViewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
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
            view.showSnackWithoutAction("HISTORY CLEARED")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}