package com.example.tr0_questionari.data

import android.util.Log
import kotlinx.serialization.Serializable
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.OutputStreamWriter

@Serializable
data class Pregunta(
    val id: Int,
    val pregunta: String,
    val respostes: List<Resposta>,
    val imatge: String
)

@Serializable
data class Resposta(
    val id: Int,
    val etiqueta: String
)

@Serializable
data class Result(
    @SerializedName("correct") val correct: Int,
    @SerializedName("incorrect") val incorrect: Int
)

suspend fun getPreguntas(url: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(url)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                throw IOException("HTTP error code: ${connection.responseCode}")
            }
        } catch (e: IOException) {
            Log.e("getPreguntes", "Network error: ${e.message}", e)
            throw e
        }
    }
}

suspend fun getJsonPreguntes(url: String): Array<Pregunta> {
    return withContext(Dispatchers.IO) {
        val json = getPreguntas(url)
        val gson = Gson()
        gson.fromJson(json, Array<Pregunta>::class.java)
    }
}

suspend fun getResults(url: String, userResponsesJson: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(url)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(userResponsesJson)
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK || connection.responseCode == HttpURLConnection.HTTP_CREATED) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                throw IOException("HTTP error code: ${connection.responseCode}")
            }
        } catch (e: IOException) {
            Log.e("getResults", "Network error: ${e.message}", e)
            throw e
        }
    }
}

suspend fun getJsonResults(url: String, userResponsesJson: String): Result {
    return withContext(Dispatchers.IO) {
        val json = getResults(url, userResponsesJson)
        Log.d("getJsonResults", "JSON response: $json")
        val gson = Gson()
        gson.fromJson(json, Result::class.java)
    }
}

suspend fun main() {
    val url = "http://a23marclacas.dam.inspedralbes.cat:25247/partida?num=10"
    val preguntes = getJsonPreguntes(url)
    preguntes.forEach { pregunta ->
        println("ID: ${pregunta.id}")
        println("Pregunta: ${pregunta.pregunta}")
        pregunta.respostes.forEach { resposta ->
            println("- Resposta: ${resposta.etiqueta}")
        }
        println("Imatge: ${pregunta.imatge}")
    }
}
