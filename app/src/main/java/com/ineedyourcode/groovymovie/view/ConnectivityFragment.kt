package com.ineedyourcode.groovymovie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ineedyourcode.groovymovie.databinding.FragmentConnectivityBinding

class ConnectivityFragment: Fragment() {
    private var _binding: FragmentConnectivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectivityBinding.inflate(inflater, container, false)
        return binding.root
    }


}