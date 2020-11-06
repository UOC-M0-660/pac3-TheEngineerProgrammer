package edu.uoc.pac3

import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Before

/**
 * Created by alex on 24/10/2020.
 */

abstract class TwitchTest {

    protected val twitchService =
        TestData.provideTwitchService(ApplicationProvider.getApplicationContext())

    @Before
    fun saveAccessToken() {
        // Launch Refresh Request
        runBlocking {
            TestData.setAccessToken(ApplicationProvider.getApplicationContext())
        }
    }
}