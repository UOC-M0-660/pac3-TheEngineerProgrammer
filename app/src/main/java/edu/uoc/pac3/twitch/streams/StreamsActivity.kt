package edu.uoc.pac3.twitch.streams

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.adapters.StreamsRecyclerAdapter
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.Stream
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.oauth.LoginActivity
import edu.uoc.pac3.tools.goToActivity
import edu.uoc.pac3.tools.playGoAnimation
import edu.uoc.pac3.twitch.profile.ProfileActivity
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
        setUpRefreshEvent()
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
        val client = Network.createHttpClient(this)
        val service = TwitchApiService(client)
        lifecycleScope.launch {
            var streamsResponse: StreamsResponse? = null
            try {
                streamsResponse = service.getStreams()
            }catch (e: UnauthorizedException){
                refreshToken(service)
                try {
                    streamsResponse = service.getStreams()
                }catch (e: UnauthorizedException){
                    logout()
                }
            }
            streamsResponse?.let {
                streamsResponse.data?.let {streams->
                    adapter.updateStreams(streams)
                }
                streamsResponse.pagination?.cursor?.let {
                    cursor = it
                }
            }?: run {
                Toast.makeText(this@StreamsActivity, getString(R.string.error_streams), Toast.LENGTH_SHORT).show()
            }
            client.close()
        }
    }

    private fun setUpEndlessRecycler(){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
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
            }
        })
    }

    private fun loadMore(){
        val client = Network.createHttpClient(this)
        val service = TwitchApiService(client)
        lifecycleScope.launch {
            var streamsResponse: StreamsResponse? = null
            try {
                streamsResponse = service.getStreams(cursor)
            }catch (e: UnauthorizedException){
                refreshToken(service)
                try {
                    streamsResponse = service.getStreams(cursor)
                }catch (e: UnauthorizedException){
                    logout()
                }
            }
            streamsResponse?.let {
                streamsResponse.data?.let {streams->
                    adapter.addStreams(streams)
                }
                streamsResponse.pagination?.cursor?.let {
                    cursor = it
                }
            }?: run {
                Toast.makeText(this@StreamsActivity, getString(R.string.error_streams), Toast.LENGTH_SHORT).show()
            }
            client.close()
        }
        isLoading = false
    }

    //He visto que hay un SwipeRefreshLayout por lo que he intentado a implementarlo
    private fun setUpRefreshEvent(){
        swipeRefreshLayout.setOnRefreshListener {
            setUpSteams()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.itemProfile -> goToProfileActivity()
        }
        return true
    }

    private fun goToProfileActivity(){
        goToActivity<ProfileActivity>()
        playGoAnimation()
    }

    private fun logout(){
        val sessionManager = SessionManager(this)
//        val client = Network.createHttpClient(this)
//        val service = TwitchApiService(client)
//        lifecycleScope.launch {
//            service.revokeToken(sessionManager.getAccessToken())
//            client.close()
//        }
        sessionManager.clearAccessToken()
        sessionManager.clearRefreshToken()
        goToActivity<LoginActivity>{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private suspend fun refreshToken(service: TwitchApiService){
        val sessionManager = SessionManager(this)
        sessionManager.clearAccessToken()
        service.getTokensRefresh(sessionManager.getRefreshToken())?.let {tokensResponse ->
            sessionManager.saveAccessToken(tokensResponse.accessToken)
            tokensResponse.refreshToken?.let {
                sessionManager.saveRefreshToken(it)
            }
        }
    }

}