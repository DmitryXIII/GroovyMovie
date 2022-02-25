package com.ineedyourcode.groovymovie.view.splashnotify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.ineedyourcode.groovymovie.R
import com.squareup.picasso.Picasso

class SplashNotifyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_notify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val poster = view.findViewById<ImageView>(R.id.splash_poster)
        Picasso.get()
            .load(
                when ((1..5).random()) {
                    1 -> "https://image.tmdb.org/t/p/w780/jT2rrplu492UFxFQIM9BzrqwLUf.jpg"
                    2 -> "https://image.tmdb.org/t/p/w780/99LE9Q1t3T8szU3iHSFPYDhPgAz.jpg"
                    3 -> "https://image.tmdb.org/t/p/w780/tRsmKCy32Y2gHPr84860HcTU6wO.jpg"
                    4 -> "https://image.tmdb.org/t/p/w780/bUT29O5UI0zYhAkglHSp8sb636B.jpg"
                    5 -> "https://image.tmdb.org/t/p/w780/s04r9V6BX1FO2INzc2DL21UW57T.jpg"
                    else -> {
                        ""
                    }
                }
            )
            .into(poster)
    }
}