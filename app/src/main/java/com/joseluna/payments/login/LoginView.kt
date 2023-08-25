package com.joseluna.payments.login


import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.joseluna.payments.ui.theme.PaymentsTheme



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(navController: NavHostController?){

    var editing by remember {
        mutableStateOf(false)
    }

    val paddingBottom: Int by animateIntAsState(
        targetValue =  if (editing) 10 else 80,
        label = "paddingBottom",
        animationSpec = tween(durationMillis = 300, easing = EaseInOut)
    )

    PaymentsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(19.dp,
                    if(editing) Alignment.CenterVertically else Alignment.Bottom
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = paddingBottom.dp)
                    .padding(horizontal = 50.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { value ->
                            editing = value.isFocused
                        },
                    value = "",
                    onValueChange = {},
                    label = { Text(text = "User") }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { value ->
                            editing = value.isFocused
                        },
                    value = "",
                    onValueChange = {},
                    label = { Text(text = "Password") }
                )
                Button(
                    onClick = {
                        navController?.let {
                            it.navigate("home"){
                                popUpTo(0)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Login")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewLogin(){
    LoginView(navController = null)
}