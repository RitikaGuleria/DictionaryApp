package com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model

data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String
)