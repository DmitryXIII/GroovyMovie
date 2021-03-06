package com.ineedyourcode.groovymovie.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ineedyourcode.groovymovie.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, MainFragment())
            .commit()
    }
}
