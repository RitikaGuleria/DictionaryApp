package com.example.dictionaryapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.ROUTE_HOME
import com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.ROUTE_SIGNUP
import com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.GoogleLogIn.SignInState
import com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.GoogleLogIn.UserData
import com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.WordInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(authViewModel: WordInfoViewModel?, navController: NavHostController?,state : SignInState,onSignInClick : () -> Unit,userData: UserData?)
{
    var email by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }

    val loginFlow= authViewModel?.loginFlow?.collectAsState()

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(56.dp),
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(text = "Dictionary App", fontSize=26.sp, color = Color.Black, textAlign = TextAlign.Center,modifier=Modifier.padding(top=16.dp), style = MaterialTheme.typography.titleMedium)
        Image(painter = painterResource(id = R.drawable.dictionary), contentDescription = "",modifier= Modifier
            .padding(32.dp)
            .size(200.dp))

        OutlinedTextField(value = email, onValueChange = {email=it},label={ Text(text = "Email") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),modifier= Modifier.padding(8.dp)
        )
        OutlinedTextField(value = password, onValueChange = {password=it},label={ Text(text = "Password") },visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),modifier= Modifier.padding(8.dp)
        )
        Button(onClick = {authViewModel?.login(email, password) },shape = RoundedCornerShape(10.dp), modifier= Modifier
            .fillMaxWidth()
            .padding(16.dp), colors = ButtonDefaults.outlinedButtonColors(Color.LightGray))
        {
            Text(text = "Log in", color = Color.Black)
        }


        SignInUsingGoogle(state,onSignInClick)


        Divider(thickness = 1.dp, modifier = Modifier.padding(top=24.dp))
        Text(text = "Don't you have an account? Sign up", fontSize = 12.sp, color = Color.DarkGray, modifier = Modifier.clickable {
            navController?.navigate(ROUTE_SIGNUP)
        })
        loginFlow?.value?.let {
            when(it){
                is Resource.Success -> LaunchedEffect(Unit){
                    navController?.navigate(ROUTE_HOME){
                        popUpTo(ROUTE_HOME){
                            inclusive=true
                        }
                    }
                }
                is Resource.Loading -> CircularProgressIndicator()

                is Resource.Error -> {
                    val context = LocalContext.current
                    Toast.makeText(context,it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewLogin()
{
//    LoginScreen(authViewModel = null, navController = null,null,null)
}