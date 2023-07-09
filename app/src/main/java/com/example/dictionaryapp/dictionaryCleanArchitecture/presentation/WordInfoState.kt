package com.example.dictionaryapp.dictionaryCleanArchitecture.presentation

import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model.WordInfo

//This is a state class which we will use in our viewmodel. We are creating state class for each screen
//that is a kind of wrapper class for the state variables that are relevant for the ui on that screen.
//so in by default we can use this wrapper class in our viewmodel to provide some state for UI.


data class WordInfoState(
    val wordInfoItems : List<WordInfo> = emptyList(),
    val isLoading : Boolean = false
)
