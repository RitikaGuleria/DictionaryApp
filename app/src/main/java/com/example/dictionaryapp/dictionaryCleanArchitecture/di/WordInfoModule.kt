package com.example.dictionaryapp.dictionaryCleanArchitecture.di

import android.app.Application
import androidx.room.Room
import com.example.dictionaryapp.dictionaryCleanArchitecture.data.local.Converters
import com.example.dictionaryapp.dictionaryCleanArchitecture.data.local.WordInfoDAO
import com.example.dictionaryapp.dictionaryCleanArchitecture.data.local.WordInfoDatabase
import com.example.dictionaryapp.dictionaryCleanArchitecture.data.remote.DictionaryAPI
import com.example.dictionaryapp.dictionaryCleanArchitecture.data.repository.WordInfoRepositoryImpl
import com.example.dictionaryapp.dictionaryCleanArchitecture.data.util.GsonParser
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.repository.WordInfoRepository
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.use_case.GetWordInfo
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordInfoModule {

    @Provides
    @Singleton
    fun provideGetWordInfoUseCase(repository: WordInfoRepository) : GetWordInfo{
        return GetWordInfo(repository)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(db:WordInfoDatabase,api: DictionaryAPI) : WordInfoRepository{
        return WordInfoRepositoryImpl(api,db.dao)
    }

    @Provides
    @Singleton
    fun provideWordInfoDatabase(app:Application) : WordInfoDatabase{
        return Room.databaseBuilder(
            app, WordInfoDatabase::class.java,"word_db"
        ).fallbackToDestructiveMigration().addTypeConverter(Converters(GsonParser(Gson()))).build()
    }

    @Provides
    @Singleton
    fun provideDictionaryApi() : DictionaryAPI{
        return Retrofit.Builder()
            .baseUrl(DictionaryAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryAPI::class.java)
    }
}