package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.pac3.R
import edu.uoc.pac3.adapters.StreamsRecyclerAdapter
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.launch

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"
    private lateinit var adapter: StreamsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()
        //Get Streams
        setUpSteams()
    }

    private fun initRecyclerView() {
        adapter = StreamsRecyclerAdapter(emptyList())
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }

    private fun setUpSteams(){
        val service = TwitchApiService(Network.createHttpClient(this))
        lifecycleScope.launch {
            service.getStreams()?.let {streamsResponse->
                streamsResponse.data?.let {streams->
                    adapter.updateStreams(streams)
                }
            }
        }
    }

}