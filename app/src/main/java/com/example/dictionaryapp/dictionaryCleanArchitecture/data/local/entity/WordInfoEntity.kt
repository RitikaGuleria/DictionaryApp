package com.example.dictionaryapp.dictionaryCleanArchitecture.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model.Meaning
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model.WordInfo

@Entity
data class WordInfoEntity(
    val word: String,
    val meanings: List<Meaning>,
    @PrimaryKey val id: Int? = null,
    val phonetic: String
)
{
    fun toWordInfo() : WordInfo{
        return WordInfo(
            word = word,
            phonetic = phonetic,
            meanings = meanings
        )
    }
}
