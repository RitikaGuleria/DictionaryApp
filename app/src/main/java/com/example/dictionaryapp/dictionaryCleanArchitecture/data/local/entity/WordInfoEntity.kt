package com.example.dictionaryapp.dictionaryCleanArchitecture.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model.Meaning
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model.WordInfo

@Entity
data class WordInfoEntity(
    val word: String,
    val photenic : String,
    val origin : String,
    val meanings : List<Meaning>,
    @PrimaryKey val id : Int?=null
)
{
    fun toWordInfo() : WordInfo{
        return WordInfo(
            word = word,
            photenic = photenic,
            origin = origin,
            meanings = meanings
        )
    }
}
