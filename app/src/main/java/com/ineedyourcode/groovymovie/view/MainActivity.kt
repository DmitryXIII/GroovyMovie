package com.ineedyourcode.groovymovie.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.view.splashnotify.SplashNotifyFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, MainFragment())
            .commit()
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras
        if (extras != null) {
            if (extras.containsKey("NOTIFY_KEY")) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.splash_container, SplashNotifyFragment())
                    .addToBackStack("")
                    .commit()
            }
        }
    }
}
