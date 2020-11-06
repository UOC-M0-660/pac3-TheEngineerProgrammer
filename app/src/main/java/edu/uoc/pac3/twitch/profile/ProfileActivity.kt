package edu.uoc.pac3.twitch.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.pac3.R

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
}