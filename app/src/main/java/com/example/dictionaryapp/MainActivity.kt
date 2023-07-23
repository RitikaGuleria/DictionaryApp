@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dictionaryapp

import android.annotation.SuppressLint
import android.app.appsearch.AppSearchResult.RESULT_OK
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.ROUTE_HOME
import com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.ROUTE_LOGIN
import com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.ROUTE_SIGNUP
import com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.GoogleLogIn.GoogleAuthUIClass
import com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.WordInfoViewModel
import com.example.dictionaryapp.ui.theme.DictionaryAppTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val viewModel by viewModels<WordInfoViewModel>()

    private val googleAuthUIClient by lazy {
        GoogleAuthUIClass(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel : WordInfoViewModel = hiltViewModel()
            DictionaryAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val navController = rememberNavController()

                    NavHost(navController = navController,startDestination= ROUTE_LOGIN)
                    {
                        composable(ROUTE_LOGIN){

                            val state by viewModel.stateGoogleSignIn.collectAsState()

                            LaunchedEffect(key1 = Unit){
                                if(googleAuthUIClient.getSignedInUser() != null)
                                {
                                    navController.navigate(ROUTE_HOME)
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = {result ->
                                    if(result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult =
                                                googleAuthUIClient.getSignInResultFromIntentOrSignInWithIntent(
                                                    intent = result.data ?: return@launch
                                                )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                })


                            LaunchedEffect(key1 = state.isSignInSuccessful){
                                if(state.isSignInSuccessful){
                                    Toast.makeText(applicationContext,"Google sign in successful...",Toast.LENGTH_LONG).show()
                                    navController.navigate(ROUTE_HOME)
                                    viewModel.resetData()

                                }
                            }

                            LoginScreen(viewModel,navController, state = state, userData = googleAuthUIClient.getSignedInUser(),onSignInClick = {
                                lifecycleScope.launch{
                                    val signInIntentSender = googleAuthUIClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            })
                        }
                        composable(ROUTE_SIGNUP){
                            SignupScreen(viewModel,navController)
                        }
                        composable(ROUTE_HOME){
                            DictionaryAppUI(viewModel,navController)
                        }

                    }
                }
            }
        }
    }

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DictionaryAppUI(viewModel: WordInfoViewModel,navController: NavHostController?)
{

    val state = viewModel.state.value
    val scaffoldState = remember{SnackbarHostState()}
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true)
    {
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is WordInfoViewModel.UIEvent.ShowSnackbar -> {
                    scaffoldState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }
    Scaffold() {
        Box(modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background))
        {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp)
                )
                {
                    OutlinedTextField(value = viewModel.searchQuery.value,
                        onValueChange = viewModel::onSearch,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = "Search a word here....")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            if (viewModel.searchQuery.value.isNotEmpty()) {
                                Icon(
                                    imageVector = Icons.Outlined.Clear,
                                    contentDescription = "",
                                    modifier = Modifier.clickable {
                                        searchQuery = ""
                                        viewModel.onSearch("")
                                    })
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                    {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                        {

                            items(state.wordInfoItems.size) { i ->
                                val wordInfo = state.wordInfoItems[i]
                                if (i > 0) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                WordInfoItem(wordInfo = wordInfo)
                                if (i < state.wordInfoItems.size - 1) {
                                    Divider()
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
        }
        Box(modifier=Modifier.wrapContentSize().padding(16.dp))
        {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                Button(
                    onClick = {
                        viewModel.logOut()
                        navController?.navigate(ROUTE_LOGIN) {
                            popUpTo(ROUTE_HOME) {
                                inclusive = true
                            }
                        }
                    }, shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Log out", fontSize = 16.sp)
                }
            }
        }

            if (state.isLoading) {
                LoadingComponentAnimation(Loading = state.isLoading)
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DictionaryAppTheme {
//        LoginScreen(authViewModel = null, navController =null)
    }
}