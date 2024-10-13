package com.example.tr0_questionari.ui

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ResultScreen(
    navController: NavController?,
    sharedViewModel: SharedViewModel = viewModel()
) {
    val activity = LocalContext.current as Activity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Resultats",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Correctes: ${sharedViewModel.correctes}",
            style = TextStyle(fontSize = 18.sp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Incorrectes: ${sharedViewModel.incorrectes}",
            style = TextStyle(fontSize = 18.sp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { navController?.popBackStack() }) {
            Text("Tornar")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { activity.finish() }) {
            Text("Sortir")
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    ResultScreen(navController = null)
}*/