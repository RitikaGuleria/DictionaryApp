package com.example.dictionaryapp.dictionaryCleanArchitecture.data.remote.dto

import com.example.dictionaryapp.dictionaryCleanArchitecture.data.local.entity.WordInfoEntity

data class WordInfoDTO(
    val meanings : List<MeaningDTO>,
    val phonetic : String,
    val phonetics : List<PhoneticDTO>,
    val word : String,
)
{
    fun toWordInfoEntity() : WordInfoEntity{
        return WordInfoEntity(
            meanings = meanings.map { it.toMeaning() },
            phonetic = phonetic,
            word = word
        )
    }
}
