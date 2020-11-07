package edu.uoc.pac3.data

import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.Stream
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by alex on 24/10/2020.
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"
    private val clientSecret = "lq5ualamkh8vlfe4ydzgsbp60529rr"//No sé si está bien poner lo aquí,
    // como dice que es secreto, pienso que no lo puedo poner lo como público

    /// Gets Access and Refresh Tokens on Twitch
    suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? = withContext(Dispatchers.IO){
        httpClient.post<OAuthTokensResponse>(Endpoints.tokenUrl){
            parameter(OAuthConstants.CLIENT_ID, OAuthConstants.clientId)
            parameter(OAuthConstants.CLIENT_SECRET, clientSecret)
            parameter(OAuthConstants.CODE, authorizationCode)
            parameter("grant_type", "authorization_code")
            parameter(OAuthConstants.REDIRECT_URI, OAuthConstants.redirectUri)
        }
    }

    /// Gets Streams on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getStreams(cursor: String? = null): StreamsResponse? = withContext(Dispatchers.IO){
        httpClient.get<StreamsResponse>(Endpoints.streamsUrl){
            header("client-id", OAuthConstants.clientId)
            cursor?.let {
                parameter("after", cursor)
            }
        }
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): User? {
        TODO("Get User from Twitch")
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): User? {
        TODO("Update User Description on Twitch")
    }
}