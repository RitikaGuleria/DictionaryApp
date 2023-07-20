package com.example.dictionaryapp

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.ROUTE_HOME
import com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.ROUTE_LOGIN
import com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.WordInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun SignupScreen(authViewModel: WordInfoViewModel?, navController: NavHostController?)
{
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val signupFlow = authViewModel?.singupFlow?.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(18.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
    {
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text(text = "Name") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ), modifier= Modifier.padding(8.dp)
        )

        OutlinedTextField(
            value= email, onValueChange = {email=it},
            label={ Text(text = "Email") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),modifier= Modifier.padding(8.dp)
        )
        OutlinedTextField(value = password, onValueChange = {password=it},
            label={ Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),modifier= Modifier.padding(8.dp)
        )
        Button(onClick = { authViewModel?.signup(name,email, password) }, shape = RoundedCornerShape(10.dp),modifier= Modifier.padding(18.dp))
        {
            Text(text="Sign up")
        }
        Text(text = "Already have an account? Sign In", fontSize = 12.sp, color = Color.DarkGray,modifier= Modifier.clickable {
            navController?.navigate(ROUTE_LOGIN)
        })

        signupFlow?.value?.let {
            when(it) {
                is Resource.Error -> {
                    val context = LocalContext.current
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Success -> {
                    LaunchedEffect(Unit){
                        navController?.navigate(ROUTE_HOME){
                            popUpTo(ROUTE_HOME){
                                inclusive=true
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSignup()
{
    SignupScreen(authViewModel = null, navController = null)
}