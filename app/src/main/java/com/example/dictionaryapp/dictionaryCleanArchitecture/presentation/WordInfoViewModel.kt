package com.example.dictionaryapp.dictionaryCleanArchitecture.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.model.WordInfo
import com.example.dictionaryapp.dictionaryCleanArchitecture.domain.use_case.GetWordInfo
import com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.data.AuthRepository
import com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.GoogleLogIn.SignInResult
import com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.GoogleLogIn.SignInState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo,private val repository: AuthRepository
) : ViewModel()
{
    private val _searchQuery = mutableStateOf("")
    val searchQuery : State<String> = _searchQuery

    private val _state = mutableStateOf(WordInfoState())
    val state : State<WordInfoState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob : Job?= null

    fun onSearch(query : String)
    {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(700L)
            Log.d("ritikaQuery",query)
            getWordInfo(query)
                .onEach { result ->
                    when(result){

                        is Resource.Success -> {
                            Log.d("ResultData",result?.data.toString())
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {

                            Log.d("ErrorData",result?.data.toString())
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(UIEvent.ShowSnackbar(
                                result.message ?: "Unknown Error"
                            ))
                        }

                        is Resource.Loading -> {

                            Log.d("LoadingData",result?.data.toString())
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    sealed class UIEvent{
        data class ShowSnackbar(val message : String) : UIEvent()
    }


    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow : StateFlow<Resource<FirebaseUser>?> = _loginFlow

    fun login(email:String,password:String)=viewModelScope.launch {
        _loginFlow.value=Resource.Loading()
        val result=repository.logIn(email, password)
        _loginFlow.value=result
    }

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val singupFlow : StateFlow<Resource<FirebaseUser>?> = _signupFlow

    fun signup(name:String,email:String,password:String)=viewModelScope.launch {
        _signupFlow.value=Resource.Loading()
        val result=repository.SignUp(name,email,password)
        _signupFlow.value=result
    }

    fun logOut()
    {
        repository.logout()
        _loginFlow.value=null
        _signupFlow.value=null
    }

    val currentUser : FirebaseUser?
        get() = repository.currentUser

    init {
        if(repository.currentUser!=null)
        {
            _loginFlow.value=Resource.Success(repository.currentUser!!)
        }
    }

    //google sign in functionality
    private val __state = MutableStateFlow(SignInState())
    val stateGoogleSignIn = __state.asStateFlow()

    fun onSignInResult(result : SignInResult){
        __state.update { it.copy(
            isSignInSuccessful = result.data!=null,
            signInError = result.errorMessage
        ) }
    }

    fun resetData(){
        __state.update { SignInState() }
    }
}