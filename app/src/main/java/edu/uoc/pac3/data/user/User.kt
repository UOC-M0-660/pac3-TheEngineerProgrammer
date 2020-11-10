package edu.uoc.pac3.data.user

import edu.uoc.pac3.data.streams.Stream
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by alex on 07/09/2020.
 */

@Serializable
data class User(
        @SerialName("display_name") val userName: String = "",
        @SerialName("description") val description: String = "",
        @SerialName("view_count") val views: Int = 0,
        @SerialName("profile_image_url") val imageUrl: String = "",
)

@Serializable
data class UserResponse(
        @SerialName("data") val data: List<User>? = null,
)