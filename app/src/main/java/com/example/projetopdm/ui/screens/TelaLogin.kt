package com.example.projetopdm.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.projetopdm.model.dados.UsuarioDAO

@Composable
fun TelaLogin(modifier: Modifier = Modifier, onSignInClick: (String) -> Unit, onSignUpClick: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    val usuarioDAO = UsuarioDAO()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Login",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = login,
                onValueChange = { login = it },
                placeholder = { Text("Login") },
                modifier = Modifier.width(280.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = senha,
                onValueChange = { senha = it },
                placeholder = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.width(280.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (login.isNotBlank() && senha.isNotBlank()) {
                        usuarioDAO.buscarPorNome(login) { usuarioEncontrado ->
                            if (usuarioEncontrado != null && usuarioEncontrado.senha == senha) {
                                onSignInClick(usuarioEncontrado.id) // Passa o userId
                            } else {
                                mensagemErro = "Login ou senha inválidos!"
                            }
                        }
                    } else {
                        mensagemErro = "Por favor, preencha todos os campos!"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00186F), contentColor = Color.White),
                modifier = Modifier.width(280.dp)
            ) {
                Text("Entrar")
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
                text = "Criar uma conta",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onSignUpClick)
            )
        }
    }
}
