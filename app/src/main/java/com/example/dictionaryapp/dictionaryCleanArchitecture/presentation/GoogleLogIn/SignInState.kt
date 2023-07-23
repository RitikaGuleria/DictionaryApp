package com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.GoogleLogIn

import java.lang.Error

data class SignInState(
    val isSignInSuccessful : Boolean = false,
    val signInError: String? = null
)
