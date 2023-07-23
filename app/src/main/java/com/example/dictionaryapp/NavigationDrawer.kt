package com.example.dictionaryapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerHeader()
{
    Box(modifier= Modifier
        .fillMaxWidth()
        .padding(vertical = 36.dp), contentAlignment = Alignment.Center)
    {
        Text(text = "Dictionary App", fontSize = 16.sp)
    }
}

@Composable
fun DrawerBody(
    items : List<MenuItem>,
    modifier: Modifier=Modifier,
    itemTextStyle : TextStyle = TextStyle(fontSize = 16.sp),
    onItemClick : (MenuItem) -> Unit
) {
    LazyColumn(modifier){
        items(items){ item ->
           Row(modifier = Modifier
               .fillMaxWidth()
               .clickable { onItemClick(item) }
               .padding(16.dp))
           {
               Icon(imageVector = item.icon, contentDescription = null)
               Spacer(modifier = Modifier.width(16.dp))

                Text(text = item.title, style = itemTextStyle, modifier=Modifier.weight(1f))
           } 
        }
    }
}

data class MenuItem(
    val id : String,
    val title : String,
    val icon : ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(onNavigationIconClick : () -> Unit)
{
    TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
        Modifier.background(MaterialTheme.colorScheme.primary), navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "")
            }
        }
        )
}