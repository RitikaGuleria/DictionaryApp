package com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.GoogleLogIn

data class SignInResult(
    val data: com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.GoogleLogIn.UserData?,
    val errorMessage: String?
)
data class UserData(
    val userId : String,
    val username : String?,
    val profilePictureUrl: String?
)