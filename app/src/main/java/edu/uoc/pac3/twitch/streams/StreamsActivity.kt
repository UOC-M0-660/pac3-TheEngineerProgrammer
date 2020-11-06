package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.pac3.R

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()
        // TODO: Get Streams
    }

    private fun initRecyclerView() {
        // TODO: Implement
    }

}