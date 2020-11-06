package edu.uoc.pac3

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.twitch.profile.ProfileActivity
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by alex on 24/10/2020.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class Ex5Test : TwitchTest() {

    @Test
    fun retrievesUserProfile() {
        runBlocking {
            val user = twitchService.getUser()
            assert(user != null) {
                "User cannot not be null"
            }
            assert(user!!.userName == TestData.userName) {
                "Invalid username"
            }
        }
    }

    @Test
    fun displaysUserProfile() {
        val scenario = ActivityScenario.launch(ProfileActivity::class.java)

        Thread.sleep(TestData.networkWaitingMillis)
        Espresso.onView(withText(TestData.userName)).check(matches(isDisplayed()))
        Espresso.onView(withText(TestData.userDescription)).check(matches(isDisplayed()))

        scenario.close()
    }

    @Test
    fun updatesUserDescription() {
        runBlocking {
            val user = twitchService.updateUserDescription(TestData.updatedUserDescription)
            assert(user!!.description == TestData.updatedUserDescription) {
                "User description not updated"
            }
            // Revert change
            twitchService.updateUserDescription(TestData.userDescription)
        }
    }

    @Test
    fun removesAccessTokenOnLogout() {
        val scenario = ActivityScenario.launch(ProfileActivity::class.java)

        // Click logout
        try {
            Espresso.onView(withId(R.id.logoutButton)).perform(click())
        } catch (e: NoMatchingViewException) {
            // Maybe logout is handled automatically
        }
        // Wait
        Thread.sleep(TestData.sharedPrefsWaitingMillis)
        // Check tokens are removed
        val sessionManager = SessionManager(ApplicationProvider.getApplicationContext())
        assert(sessionManager.getAccessToken() == null) {
            "Access token needs to be removed on logout"
        }
        assert(sessionManager.getRefreshToken() == null) {
            "Refresh token needs to be removed on logout"
        }
        // Restore tokens
        runBlocking {
            TestData.setAccessToken(ApplicationProvider.getApplicationContext())
        }

        scenario.close()
    }

}