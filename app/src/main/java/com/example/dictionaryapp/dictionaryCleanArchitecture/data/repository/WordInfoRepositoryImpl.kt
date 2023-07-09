package com.example.dictionaryapp.dictionaryCleanArchitecture.data.repository

import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.dictionaryCleanArchitecture.data.local.WordInfoDAO
import com.example.dictionaryapp.dictionaryCleanArchitecture.data.remote.DictionaryAPI
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model.WordInfo
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryAPI,
    private val dao : WordInfoDAO
) : WordInfoRepository
{
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow{

        emit(Resource.Loading())
        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() } //cache words
        emit(Resource.Loading(data=wordInfos)) // this will notify to the viewmodel that there is a wordinfos to display in th U.I.

        // making api request and initiate that

        try {
            val remoteWordInfos = api.getWordInfo(word)
            dao.deleteWordInfos(remoteWordInfos.map { it.word })
            dao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })

        } catch (e: HttpException)
        {
            emit(Resource.Error(
                message = "Oops!!Something went wrong",
                data = wordInfos
            ))
        } catch (e : IOException)
        {
            emit(Resource.Error(
                message = "Couldn't reach server,check your internet connection!!",
                data = wordInfos
            ))
        }

        //caching logic to provide a word to the ui
        val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfos))
    }
}