package com.ineedyourcode.groovymovie.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ineedyourcode.groovymovie.R

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onNewIntent(intent)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, MainFragment())
            .commit()
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent?) {
        val extras: Bundle? = intent?.extras
        if (extras != null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_fragment_container, MainFragment())
                .addToBackStack("")
                .commit()
        }
    }
}
