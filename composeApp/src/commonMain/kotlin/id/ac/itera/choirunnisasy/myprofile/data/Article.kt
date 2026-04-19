package id.ac.itera.choirunnisasy.myprofile.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Int,
    @SerialName("userId") val userId: Int,
    val title: String,
    val body: String
)

@Serializable
data class Photo(
    val id: Int,
    @SerialName("albumId") val albumId: Int,
    val title: String,
    val url: String,
    @SerialName("thumbnailUrl") val thumbnailUrl: String
)

data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val thumbnailUrl: String
)

@Serializable
data class CreatePostRequest(
    val title: String,
    val body: String,
    val userId: Int = 1 // Dummy user ID
)

@Serializable
data class CreatePostResponse(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)