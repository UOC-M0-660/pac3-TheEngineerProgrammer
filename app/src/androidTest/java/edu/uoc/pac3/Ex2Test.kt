package edu.uoc.pac3

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import edu.uoc.pac3.data.SessionManager
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

/**
 * Created by alex on 24/10/2020.
 */

@LargeTest
class Ex2Test {

    @After
    fun restoreCorrectAccessToken() {
        val context: Context = ApplicationProvider.getApplicationContext()
        runBlocking {
            TestData.setAccessToken(context)
        }
    }

    @Test
    fun sessionManagerSavesOAuthTokens() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val sessionManager = SessionManager(context)
        // Save tokens
        sessionManager.saveAccessToken(TestData.dummyAccessToken)
        sessionManager.saveRefreshToken(TestData.dummyRefreshToken)
        // Give some time for Shared Prefs
        Thread.sleep(TestData.sharedPrefsWaitingMillis)
        // Assert tokens are saved
        assert(sessionManager.getAccessToken() == TestData.dummyAccessToken)
        assert(sessionManager.getRefreshToken() == TestData.dummyRefreshToken)
    }

    @Test
    fun sessionManagerClearsOAuthTokens() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val sessionManager = SessionManager(context)
        // Save tokens
        sessionManager.saveAccessToken(TestData.dummyAccessToken)
        sessionManager.saveRefreshToken(TestData.dummyRefreshToken)
        // Give some time for Shared Prefs
        Thread.sleep(TestData.sharedPrefsWaitingMillis)
        // Clear tokens
        sessionManager.clearAccessToken()
        sessionManager.clearRefreshToken()
        // Give some time for Shared Prefs
        Thread.sleep(TestData.sharedPrefsWaitingMillis)
        // Assert tokens are cleared
        assert(sessionManager.getAccessToken() == null)
        assert(sessionManager.getRefreshToken() == null)
    }
}