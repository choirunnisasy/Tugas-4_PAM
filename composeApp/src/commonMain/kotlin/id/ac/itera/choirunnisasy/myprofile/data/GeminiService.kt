package id.ac.itera.choirunnisasy.myprofile.data

import id.ac.itera.choirunnisasy.myprofile.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import io.ktor.utils.io.*

class GeminiService(private val client: HttpClient) {
    private val apiKey = BuildConfig.GEMINI_API_KEY
    // Menggunakan gemini-2.0-flash
    private val modelName = "gemini-2.0-flash"
    private val streamUrl = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:streamGenerateContent"

    fun streamGenerateContent(request: GeminiRequest): Flow<GeminiResponse> = flow {
        client.preparePost("$streamUrl?key=$apiKey&alt=sse") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.execute { response ->
            if (response.status != HttpStatusCode.OK) {
                // Lempar kode error untuk ditangkap AIRepository
                throw Exception("${response.status.value}")
            }
            
            val channel = response.bodyAsChannel()
            while (!channel.isClosedForRead) {
                val line = channel.readUTF8Line(Int.MAX_VALUE) ?: break
                if (line.startsWith("data: ")) {
                    val jsonString = line.substring(6).trim()
                    if (jsonString.isNotEmpty()) {
                        try {
                            val geminiResponse = Json { ignoreUnknownKeys = true }.decodeFromString<GeminiResponse>(jsonString)
                            emit(geminiResponse)
                        } catch (e: Exception) { }
                    }
                }
            }
        }
    }
}
