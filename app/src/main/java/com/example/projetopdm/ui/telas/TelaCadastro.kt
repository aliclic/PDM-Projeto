package com.example.projetopdm.ui.telas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TelaCadastro(modifier: Modifier = Modifier, onSignUpClick: () -> Unit, onSignInClick: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = nome,
                onValueChange = { nome = it },
                placeholder = { Text("Nome") }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = senha,
                onValueChange = { senha = it },
                placeholder = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                if (nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank()) {
                    onSignUpClick()
                } else {
                    mensagemErro = "Por favor, preencha todos os campos!"
                }
            }) {
                Text("Cadastrar")
            }
            mensagemErro?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
                LaunchedEffect(Unit) {
                    delay(3000)
                    mensagemErro = null
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Entrar",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onSignInClick)
            )
        }
    }
}
