package edu.uoc.pac3.oauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.uoc.pac3.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Login with Twitch
        twitchLoginButton.setOnClickListener {
            startActivity(Intent(this, OAuthActivity::class.java))
        }
    }
}