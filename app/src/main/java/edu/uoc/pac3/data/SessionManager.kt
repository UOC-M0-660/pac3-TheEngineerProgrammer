package edu.uoc.pac3.data

import android.content.Context

/**
 * Created by alex on 06/09/2020.
 */

class SessionManager(context: Context) {

    fun isUserAvailable(): Boolean {
        // TODO: Implement
        return false
    }

    fun getAccessToken(): String? {
        // TODO: Implement
        return null
    }

    fun saveAccessToken(accessToken: String) {
        TODO("Save Access Token")
    }

    fun clearAccessToken() {
        TODO("Clear Access Token")
    }

    fun getRefreshToken(): String? {
        TODO("Get Refresh Token")
    }

    fun saveRefreshToken(refreshToken: String) {
        TODO("Save Refresh Token")
    }

    fun clearRefreshToken() {
        TODO("Clear Refresh Token")
    }

}