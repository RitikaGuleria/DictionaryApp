package com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.data

import com.example.dictionaryapp.core.util.Resource
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun logIn(email:String,password:String) : Resource<FirebaseUser>
    suspend fun SignUp(name:String,email:String,password: String) : Resource<FirebaseUser>
    fun logout()
}