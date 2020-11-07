package edu.uoc.pac3.data.streams

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Created by alex on 07/09/2020.
 */

@Serializable
data class Stream(
    @SerialName("user_name") val userName: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null
)

@Serializable
data class StreamsResponse(
        @SerialName("data") val data: List<Stream>? = null,
        @SerialName("pagination") val pagination: Pagination? = null
)

@Serializable
data class Pagination(
        @SerialName("cursor") val cursor: String? = null
)