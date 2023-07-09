package com.example.dictionaryapp.dictionaryCleanArchitecture.data.remote

import com.example.dictionaryapp.dictionaryCleanArchitecture.data.remote.dto.WordInfoDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryAPI {

    @GET("/api/v2/entries/en/{word}")
    suspend fun getWordInfo(
        @Path("word") word : String
    ) : List<WordInfoDTO>

    companion object{
        const val BASE_URL = "https://api.dictionaryapi.dev/"
    }
}