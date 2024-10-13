package com.example.tr0_questionari

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tr0_questionari.data.Pregunta
import com.example.tr0_questionari.data.getJsonPreguntes
import com.example.tr0_questionari.ui.QuestionariScreen
import com.example.tr0_questionari.ui.ResultScreen
import com.example.tr0_questionari.ui.SharedViewModel
import com.example.tr0_questionari.ui.StartScreen
import com.example.tr0_questionari.ui.theme.TR0_QuestionariTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TR0_QuestionariTheme {

                val llistaPreguntes = remember { mutableStateOf<Array<Pregunta>>(arrayOf()) }

                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        llistaPreguntes.value = getJsonPreguntes("http://a23marclacas.dam.inspedralbes.cat:25247/partida?num=10")
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    MyApp(llistaPreguntes)
                }
            }
        }
    }
}

@Composable
fun MyApp(llistaPreguntes: MutableState<Array<Pregunta>>) {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()
    NavHost(navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("questionari") { QuestionariScreen(llistaPreguntes, navController, sharedViewModel) }
        composable("result") { ResultScreen(navController, sharedViewModel) }
    }
}