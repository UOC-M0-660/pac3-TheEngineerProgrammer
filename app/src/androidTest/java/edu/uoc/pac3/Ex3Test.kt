package edu.uoc.pac3

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import edu.uoc.pac3.twitch.streams.StreamsActivity
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by alex on 24/10/2020.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class Ex3Test : TwitchTest() {

    @Test
    fun retrievesStreams() {
        runBlocking {
            val streams = twitchService.getStreams()
            assert(!streams?.data.isNullOrEmpty()) {
                "Streams response cannot be empty"
            }
        }
    }

    @Test
    fun listDisplaysStreamProperties() {
        // Start Activity
        val scenario = ActivityScenario.launch(StreamsActivity::class.java)

        // Get Streams
        val streams = runBlocking {
            twitchService.getStreams()?.data.orEmpty()
        }
        // Give some time for actual data to be loaded on RecyclerView
        Thread.sleep(TestData.networkWaitingMillis)
        // Check they are displayed in list
        Espresso.onView(ViewMatchers.withText(streams.first().userName ?: ""))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(streams.first().title ?: ""))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // End Activity
        scenario.close()
    }

}