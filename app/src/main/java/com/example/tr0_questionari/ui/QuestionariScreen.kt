package com.example.tr0_questionari.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tr0_questionari.data.Pregunta
import com.example.tr0_questionari.data.getJsonResults
import com.google.gson.Gson
import kotlinx.serialization.Serializable
import androidx.compose.runtime.MutableState as MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Serializable
data class RespostaUsuari(
    val resposta: Int,
    val pregunta: Int
)

@Composable
fun QuestionariScreen(
    preguntes: MutableState<Array<Pregunta>>,
    navcontroller: NavController,
    sharedViewModel: SharedViewModel = viewModel()
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = preguntes.value[currentQuestionIndex].pregunta,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        AsyncImage(
            model = preguntes.value[currentQuestionIndex].imatge,
            contentDescription = "Question Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        preguntes.value[currentQuestionIndex].respostes.forEachIndexed { index, resposta ->
            Button(
                onClick = {
                    sharedViewModel.userResponses.add(RespostaUsuari(index, preguntes.value[currentQuestionIndex].id))
                    Log.d("QuestionariScreen", "Resposta seleccionada: ${resposta.etiqueta}")
                    if (currentQuestionIndex == preguntes.value.size - 1) {
                        val gson = Gson()
                        sharedViewModel.userResponsesJson = gson.toJson(sharedViewModel.userResponses)
                        Log.d("QuestionariScreen", "User responses JSON: ${sharedViewModel.userResponsesJson}")
                        sharedViewModel.userResponses.clear()
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val result = getJsonResults("http://a23marclacas.dam.inspedralbes.cat:25247/respostes", sharedViewModel.userResponsesJson)
                                withContext(Dispatchers.Main) {
                                    sharedViewModel.correctes = result.correct
                                    sharedViewModel.incorrectes = result.incorrect
                                }
                            } catch (e: Exception) {
                                Log.e("QuestionariScreen", "Error getting results", e)
                            }
                        }
                        navcontroller.navigate("result")
                    } else {
                        currentQuestionIndex = (currentQuestionIndex + 1).coerceAtMost(preguntes.value.size - 1)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(text = resposta.etiqueta)
            }
        }
    }
}
