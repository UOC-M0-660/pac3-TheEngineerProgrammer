package edu.uoc.pac3.data.streams


/**
 * Created by alex on 07/09/2020.
 */

data class Stream(
    val userName: String? = null,
    val title: String? = null,
)

data class StreamsResponse(
    val data: List<Stream>? = null,
)