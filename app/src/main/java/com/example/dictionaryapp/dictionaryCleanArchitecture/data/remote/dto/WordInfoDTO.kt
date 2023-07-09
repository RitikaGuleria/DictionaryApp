package com.example.dictionaryapp.dictionaryCleanArchitecture.data.remote.dto

import com.example.dictionaryapp.dictionaryCleanArchitecture.data.local.entity.WordInfoEntity
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model.WordInfo

data class WordInfoDTO(
    val meanings : List<MeaningDTO>,
    val origin : String,
    val photenic : String,
    val photenics : List<PhoneticDTO>,
    val word : String
)
{
    fun toWordInfoEntity() : WordInfoEntity{
        return WordInfoEntity(
            meanings = meanings.map { it.toMeaning() },
            origin = origin,
            photenic = photenic,
            word = word
        )
    }
}
