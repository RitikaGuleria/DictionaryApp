package com.example.dictionaryapp.dictionaryCleanArchitecture.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dictionaryapp.dictionaryCleanArchitecture.data.local.entity.WordInfoEntity

@Database(entities = [WordInfoEntity::class] , version = 2)
@TypeConverters(Converters::class)

abstract class WordInfoDatabase : RoomDatabase() {
    abstract val dao : WordInfoDAO
}