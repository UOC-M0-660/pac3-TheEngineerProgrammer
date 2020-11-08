package edu.uoc.pac3.oauth

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.tools.goToActivity
import edu.uoc.pac3.tools.playGoAnimation
import edu.uoc.pac3.twitch.streams.StreamsActivity
import kotlinx.android.synthetic.main.activity_oauth.*
import kotlinx.coroutines.launch
import java.util.*

class OAuthActivity : AppCompatActivity() {

    private val TAG = "OAuthActivity"
    private val uniqueState = UUID.randomUUID().toString()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        launchOAuthAuthorization()
    }

    fun buildOAuthUri(): Uri {
        return Uri.parse(Endpoints.authorizationUrl)
            .buildUpon()
            .appendQueryParameter(OAuthConstants.CLIENT_ID, OAuthConstants.clientId)
            .appendQueryParameter(OAuthConstants.REDIRECT_URI, OAuthConstants.redirectUri)
            .appendQueryParameter(OAuthConstants.RESPONSE_TYPE, OAuthConstants.CODE)
            .appendQueryParameter(OAuthConstants.SCOPE, OAuthConstants.scopes.joinToString(separator = " "))
            .appendQueryParameter(OAuthConstants.STATE, uniqueState)
            .build()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun launchOAuthAuthorization() {
        //  Create URI
        val uri = buildOAuthUri()

        // Set webView Redirect Listener
        setUpWebRedirectListener()

        // Load OAuth Uri
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(uri.toString())
    }

    //Set webView Redirect Listener
    private fun setUpWebRedirectListener(){
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.let {
                    if (request.url.toString().startsWith(OAuthConstants.redirectUri)){
                        val responseState = request.url.getQueryParameter(OAuthConstants.STATE)
                        if (responseState == uniqueState){
                            request.url.getQueryParameter(OAuthConstants.CODE)?.let {code ->
                                onAuthorizationCodeRetrieved(code)
                                webView.visibility = View.GONE
                                progressBar.visibility = View.VISIBLE

                            }?: let {
                                Toast.makeText(this@OAuthActivity, getString(R.string.error_oauth), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
    }

    // Call this method after obtaining the authorization code
    // on the WebView to obtain the tokens
    private fun onAuthorizationCodeRetrieved(authorizationCode: String) {
        // Show Loading Indicator
        progressBar.visibility = View.VISIBLE
        // Create Twitch Service
        val client = Network.createHttpClient(this)
        val twitchService = TwitchApiService(client)
        // Get Tokens from Twitch
        lifecycleScope.launch {
            val tokensResponse = twitchService.getTokens(authorizationCode)
            tokensResponse?.let {
                val sessionManager = SessionManager(this@OAuthActivity)
                sessionManager.saveAccessToken(tokensResponse.accessToken)
                tokensResponse.refreshToken?.let {
                    sessionManager.saveRefreshToken(tokensResponse.refreshToken)
                }
                client.close()
                goToStreamsActivity()
            } ?: run {
                webView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                Toast.makeText(this@OAuthActivity, getString(R.string.error_oauth), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun goToStreamsActivity(){
        goToActivity<StreamsActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        playGoAnimation()
    }

}