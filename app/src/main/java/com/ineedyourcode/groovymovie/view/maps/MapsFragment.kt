package com.ineedyourcode.groovymovie.view.maps

import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMapsBinding
import java.io.IOException

private const val ARG_TARGET_LOCATION = "TARGET_LOCATION"

class MapsFragment : Fragment() {
    val TAG_FOR_BACKSTACK = "MapsFragment"

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    private lateinit var targetLocation: String

    companion object {
        fun newInstance(targetLocation: String) = MapsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TARGET_LOCATION, targetLocation)
            }
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        targetLocation = if (arguments != null) {
            requireArguments().getString(ARG_TARGET_LOCATION).toString()
        } else {
            "Shawnee, Oklahoma, USA"
        }
        initSearchByAddress(targetLocation)
    }

    private fun initSearchByAddress(targetLocation: String) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocationName(targetLocation, 1)
                if (addresses.size > 0) {
                    goToAddress(addresses, requireView(), targetLocation)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun goToAddress(
        addresses: MutableList<Address>,
        view: View,
        searchText: String
    ) {
        val location = LatLng(
            addresses[0].latitude,
            addresses[0].longitude
        )
        view.post {
            setMarker(location, searchText)
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    10f
                )
            )
        }
    }

    private fun setMarker(
        location: LatLng,
        searchText: String
    ): Marker? {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
        )
    }
}