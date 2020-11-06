package edu.uoc.pac3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.uoc.pac3.oauth.LoginActivity
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.twitch.streams.StreamsActivity

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        checkUserSession()
    }

    private fun checkUserSession() {
        if (SessionManager(this).isUserAvailable()) {
            // User is available, open Streams Activity
            startActivity(Intent(this, StreamsActivity::class.java))
        } else {
            // User not available, request Login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}