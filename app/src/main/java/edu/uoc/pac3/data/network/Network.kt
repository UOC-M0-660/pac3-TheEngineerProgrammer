package edu.uoc.pac3.data.network

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

/**
 * Created by alex on 07/09/2020.
 */
object Network {

    private const val TAG = "Network"

    fun createHttpClient(context: Context): HttpClient {
        return HttpClient(OkHttp) {
            // TODO: Setup HttpClient
        }
    }
}