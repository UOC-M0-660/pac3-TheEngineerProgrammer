package edu.uoc.pac3.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys


/**
 * Created by alex on 06/09/2020.
 */
//Clase para manejar sharedPreferences
class SessionManager(private val context: Context) {

    companion object{
        private const val SECRET_FILE_NAME = "secret_shared_prefs"
        private const val FILE_NAME = "shared_prefs"
        private const val ACCESS_TOKEN = "accessToken"
        private const val REFRESH_TOKEN = "refreshToken"
    }

    private fun getSecretSharedPreferences(): SharedPreferences{
        val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        return EncryptedSharedPreferences.create(
                context,
                SECRET_FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getSharedPreferences(): SharedPreferences{
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    fun isUserAvailable(): Boolean {
        // TODO: Implement
        return false
    }

    fun getAccessToken(): String {
        return getSecretSharedPreferences().getString(ACCESS_TOKEN, "") ?: ""
    }

    fun saveAccessToken(accessToken: String) {
        getSecretSharedPreferences().edit().putString(ACCESS_TOKEN, accessToken).apply()
    }

    fun clearAccessToken() {
        getSecretSharedPreferences().edit().putString(ACCESS_TOKEN, "").apply()
    }

    fun getRefreshToken(): String {
        return getSecretSharedPreferences().getString(REFRESH_TOKEN, "") ?: ""
    }

    fun saveRefreshToken(refreshToken: String) {
        getSecretSharedPreferences().edit().putString(REFRESH_TOKEN, refreshToken).apply()
    }

    fun clearRefreshToken() {
        getSecretSharedPreferences().edit().putString(REFRESH_TOKEN, "").apply()
    }

}