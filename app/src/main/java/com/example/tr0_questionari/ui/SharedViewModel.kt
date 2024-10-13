package com.example.tr0_questionari.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val userResponses = mutableStateListOf<RespostaUsuari>()
    var userResponsesJson = String()
    var correctes by mutableIntStateOf(0)
    var incorrectes by mutableIntStateOf(0)
}