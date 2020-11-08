package edu.uoc.pac3.twitch.profile


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.user.User
import edu.uoc.pac3.oauth.LoginActivity
import edu.uoc.pac3.tools.goToActivity
import edu.uoc.pac3.tools.playBackAnimation
import edu.uoc.pac3.tools.playGoAnimation
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        loadUserInfoAndUpdateUI()
        setUpEvents()
    }

    //cargo los datos de Twitch
    private fun loadUserInfoAndUpdateUI(){
        val client = Network.createHttpClient(this)
        val service = TwitchApiService(client)
        lifecycleScope.launch {
            var user: User? = null
            try {
                user = service.getUser()
            }catch (e: UnauthorizedException){
                refreshToken(service)
                try {
                    user = service.getUser()
                }catch (e: UnauthorizedException){
                    logout()
                }
            }
            user?.let {
                updateUI(it)
            }
            client.close()
        }
    }

    //refrescar token
    private suspend fun refreshToken(service: TwitchApiService){
        val sessionManager = SessionManager(this)
        sessionManager.clearAccessToken()
        service.getTokensRefresh(sessionManager.getRefreshToken())?.let {tokensResponse ->
            sessionManager.saveAccessToken(tokensResponse.accessToken)
            tokensResponse.refreshToken?.let {
                sessionManager.saveRefreshToken(it)
            }
        }
    }

    //actualizar UI
    private fun updateUI(user: User){
        userNameTextView.text = user.userName
        userDescriptionEditText.setText(user.description)
        viewsText.text = user.views.toString()
        if (user.imageUrl.isNotEmpty()){
            Picasso.get().load(user.imageUrl).into(imageViewUser)
        }
    }

    //eventos de cerrar sesión y cambiar descripción
    private fun setUpEvents(){
        updateDescriptionButton.setOnClickListener {
            val description = userDescriptionEditText.text.toString()
            if (description.isBlank()){
                userDescriptionEditText.error = getString(R.string.no_empty_description)
                return@setOnClickListener
            }
            updateDescription(description)
        }
        logoutButton.setOnClickListener {
            logout()
        }
    }

    //actualizar la descripción
    private fun updateDescription(description: String){
        val client = Network.createHttpClient(this)
        val service = TwitchApiService(client)
        lifecycleScope.launch {
            var user: User? = null
            try {
                user = service.updateUserDescription(description)
            }catch (e: UnauthorizedException){
                refreshToken(service)
                try {
                    user = service.updateUserDescription(description)
                }catch (e: UnauthorizedException){
                    logout()
                }
            }

            user?.let {
                userDescriptionEditText.clearFocus()
                Toast.makeText(this@ProfileActivity, getString(R.string.description_success), Toast.LENGTH_SHORT).show()
            }?: run{
                Toast.makeText(this@ProfileActivity, getString(R.string.error_profile_update), Toast.LENGTH_SHORT).show()
            }
            client.close()
        }
    }

    private fun logout(){
        val sessionManager = SessionManager(this)
//        val client = Network.createHttpClient(this)
//        val service = TwitchApiService(client)
//        lifecycleScope.launch {
//            service.revokeToken(sessionManager.getAccessToken())
//            client.close()
//        }
        sessionManager.clearAccessToken()
        sessionManager.clearRefreshToken()
        goToActivity<LoginActivity>{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home ->{
                finish()
                playBackAnimation()
            }
        }
        return true
    }

    override fun onBackPressed() {
        finish()
        playBackAnimation()
    }
}