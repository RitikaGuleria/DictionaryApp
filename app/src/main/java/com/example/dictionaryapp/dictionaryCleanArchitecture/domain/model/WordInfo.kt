package com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model


data class WordInfo(
    val meanings : List<Meaning>,
    val origin : String,
    val photenic : String,
    val word : String
)
