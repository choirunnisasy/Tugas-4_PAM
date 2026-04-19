package id.ac.itera.choirunnisasy.myprofile.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class NewsRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val baseUrl = "https://jsonplaceholder.typicode.com"

    suspend fun getArticles(): Result<List<Article>> {
        return try {
            val posts: List<Post> = client.get("$baseUrl/posts").body()

            // Tidak perlu fetch photos lagi, pakai Picsum langsung
            val articles = posts.take(20).mapIndexed { index, post ->
                Article(
                    id           = post.id,
                    title        = post.title.replaceFirstChar { it.uppercase() },
                    description  = post.body,
                    imageUrl     = "https://picsum.photos/seed/${post.id}/600/400",
                    thumbnailUrl = "https://picsum.photos/seed/${post.id}/200/200"
                )
            }
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArticleById(id: Int): Result<Article> {
        return try {
            val post: Post = client.get("$baseUrl/posts/$id").body()

            Result.success(
                Article(
                    id           = post.id,
                    title        = post.title.replaceFirstChar { it.uppercase() },
                    description  = post.body,
                    imageUrl     = "https://picsum.photos/seed/$id/600/400",
                    thumbnailUrl = "https://picsum.photos/seed/$id/200/200"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPost(title: String, body: String): Result<Boolean> {
        return try {
            val response: CreatePostResponse = client.post("$baseUrl/posts") {
                contentType(ContentType.Application.Json)
                setBody(CreatePostRequest(title = title, body = body))
            }.body()

            // Jika berhasil mengembalikan ID dari JSONPlaceholder, berarti POST sukses!
            Result.success(response.id > 0)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}