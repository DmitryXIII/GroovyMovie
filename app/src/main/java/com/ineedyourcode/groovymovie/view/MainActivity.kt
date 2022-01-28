package com.ineedyourcode.groovymovie.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.Observer
import com.ineedyourcode.groovymovie.utils.ConnectivityLiveData
import com.ineedyourcode.groovymovie.R

class MainActivity : AppCompatActivity() {

    private var isConnected = true

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.fragment_container, MainScreenFragment())
            .commit()

        ConnectivityLiveData(this)
            .observe(this, Observer { isConnectFromReceiver ->
                if (!isConnectFromReceiver && isConnected) {
                    isConnected = false
                    supportFragmentManager
                        .beginTransaction()
                        .setTransition(TRANSIT_FRAGMENT_FADE)
                        .replace(
                            R.id.fragment_no_connection_container,
                            ConnectivityFragment(),
                            "NO_CONNECTION_FRAGMENT"
                        )
                        .commit()
                } else if (isConnectFromReceiver && isConnected == false) {
                    isConnected = true
                    supportFragmentManager
                        .findFragmentByTag("NO_CONNECTION_FRAGMENT")?.let { fragment ->
                            supportFragmentManager
                                .beginTransaction()
                                .remove(fragment)
                                .commit()
                        }
                }
            })
    }
}
