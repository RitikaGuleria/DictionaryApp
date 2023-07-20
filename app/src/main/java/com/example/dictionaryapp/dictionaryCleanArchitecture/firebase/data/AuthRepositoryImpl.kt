package com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.data

import com.example.dictionaryapp.core.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun logIn(email: String, password: String): Resource<FirebaseUser>
    {
        return try {
            val result=firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun SignUp(name: String, email: String, password: String, ): Resource<FirebaseUser>
    {
        return try {
            val result=firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            Resource.Success(result.user!!)
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}