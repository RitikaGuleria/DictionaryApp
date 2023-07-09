package com.example.dictionaryapp.dictionaryCleanArchitecture.data.remote.dto

import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model.Meaning

data class MeaningDTO(
    val definitions: List<DefinitionDTO>,
    val partOfSpeech : String
){
    fun toMeaning() : Meaning {
        return Meaning(
            definitions = definitions.map { it.toDefinition() },
            partOfSpeech = partOfSpeech
        )
    }
}