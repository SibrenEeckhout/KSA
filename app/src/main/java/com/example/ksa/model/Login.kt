package com.example.ksa.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ksa.R
import com.example.ksa.ui.LoginViewModel

class Login {
    companion object {
        @Composable
        fun LoginPage(
            loginViewModel: LoginViewModel,
        ) {
            val loginUiState by loginViewModel.uiState.collectAsState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ksalogotransparant),
                    contentDescription = null,
                    modifier = Modifier
                        .size(159.dp)
                        .padding(bottom = 30.dp)
                )

                TextField(
                    value = loginUiState.emailText,
                    onValueChange = { loginViewModel.onEmailChange(it) },
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                )

                TextField(
                    value = loginUiState.passwordText,
                    onValueChange = { loginViewModel.onPasswordChange(it) },
                    label = { Text(stringResource(R.string.password)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    visualTransformation = PasswordVisualTransformation(),
                )

                Button(
                    onClick = { loginViewModel.login() },
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(text = stringResource(R.string.login))
                }

                Text(
                    text = loginUiState.error,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    }
}