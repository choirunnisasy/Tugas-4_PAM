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
    
    companion object {
        // Menggunakan gemini-2.5-flash sesuai permintaan.
        // Jika muncul error 429 (Limit 0), berarti model ini belum tersedia untuk akun Anda.
        private const val MODEL_NAME = "gemini-2.5-flash"
        private const val HOST = "generativelanguage.googleapis.com"
    }

    private val apiKey = BuildConfig.GEMINI_API_KEY

    fun streamGenerateContent(request: GeminiRequest): Flow<GeminiResponse> = flow {
        if (apiKey.isBlank()) {
            throw Exception("API Key kosong! Periksa local.properties.")
        }

        client.preparePost {
            url {
                protocol = URLProtocol.HTTPS
                host = HOST
                // Menggunakan v1beta untuk mendukung model terbaru/preview
                encodedPath = "/v1beta/models/$MODEL_NAME:streamGenerateContent"
                parameters.append("key", apiKey)
                parameters.append("alt", "sse")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.execute { response ->
            if (response.status != HttpStatusCode.OK) {
                val errorBody = response.bodyAsText()
                throw Exception("Google AI Error (HTTP ${response.status.value}): $errorBody")
            }
            
            val channel = response.bodyAsChannel()
            while (!channel.isClosedForRead) {
                val line = channel.readUTF8Line(Int.MAX_VALUE) ?: break
                if (line.startsWith("data: ")) {
                    val jsonString = line.substring(6).trim()
                    if (jsonString.isNotEmpty()) {
                        try {
                            val geminiResponse = Json { 
                                ignoreUnknownKeys = true 
                            }.decodeFromString<GeminiResponse>(jsonString)
                            
                            if (geminiResponse.error != null) {
                                throw Exception(geminiResponse.error.message)
                            }
                            
                            emit(geminiResponse)
                        } catch (e: Exception) { }
                    }
                }
            }
        }
    }
}
