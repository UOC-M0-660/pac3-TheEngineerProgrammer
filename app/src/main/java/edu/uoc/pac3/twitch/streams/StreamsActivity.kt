package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.adapters.StreamsRecyclerAdapter
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.streams.Stream
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.launch

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"
    private lateinit var adapter: StreamsRecyclerAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading = false
    private var cursor = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()
        //Get Streams
        setUpSteams()
        setUpEndlessRecycler()
    }

    private fun initRecyclerView() {
        adapter = StreamsRecyclerAdapter(mutableListOf())
        layoutManager = LinearLayoutManager(this)
        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
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
                streamsResponse.pagination?.cursor?.let {
                    cursor = it
                }
            }
        }
    }

    private fun setUpEndlessRecycler(){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isLoading) {
                    val visibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val totalItemCount = adapter.itemCount
                    if (visibleItemPosition == totalItemCount - 1) {
                        isLoading = true
                        recyclerView.post {
                            loadMore()
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun loadMore(){
        val service = TwitchApiService(Network.createHttpClient(this))
        lifecycleScope.launch {
            service.getStreams(cursor)?.let {streamsResponse->
                streamsResponse.data?.let {streams->
                    adapter.addStreams(streams)
                }
                streamsResponse.pagination?.cursor?.let {
                    cursor = it
                }
            }
        }
        isLoading = false
    }

}