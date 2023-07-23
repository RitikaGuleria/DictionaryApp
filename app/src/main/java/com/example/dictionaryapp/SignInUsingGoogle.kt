package com.example.dictionaryapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.ROUTE_LOGIN
import com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.GoogleLogIn.SignInState
import com.example.dictionaryapp.dictionaryCleanArchitecture.presentation.WordInfoViewModel

@Composable
fun SignInUsingGoogle(state : SignInState,onSignInClick : () -> Unit)
{
    val context = LocalContext.current
    
    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let { error ->
            Toast.makeText(context,error,Toast.LENGTH_LONG).show()
        }
    }

    Button(onClick = onSignInClick , colors = ButtonDefaults.outlinedButtonColors(Color.White),shape = RoundedCornerShape(10.dp), modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp))
    {
        Row()
        {
            Image(painter = painterResource(id = R.drawable.googleimg), contentDescription = null,modifier= Modifier
                .size(27.dp)
                .padding(1.dp)
                .weight(0.2f))
            
            Text(text = "Sign in using Google", textAlign = TextAlign.Center,color= Color.Black, fontSize = 13.sp, modifier = Modifier.weight(0.8f))
        }
    }
}

@Preview
@Composable
fun pp()
{
//    SignInUsingGoogle(null,null)
}