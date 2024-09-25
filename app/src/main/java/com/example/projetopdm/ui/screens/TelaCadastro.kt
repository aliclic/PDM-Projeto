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
import com.example.projetopdm.model.dados.Usuario
import com.example.projetopdm.model.dados.UsuarioDAO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@Composable
fun TelaCadastro(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var nickName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }
    var mensagemSucesso by remember { mutableStateOf<String?>(null) }

    val usuarioDAO = UsuarioDAO()
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Cadastro",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = nome,
                onValueChange = { nome = it },
                placeholder = { Text("Nome") },
                modifier = Modifier.width(280.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = nickName,
                onValueChange = { nickName = it },
                placeholder = { Text("NickName") },
                modifier = Modifier.width(280.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email") },
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
                    if (nome.isNotBlank() && nickName.isNotBlank() && email.isNotBlank() && senha.isNotBlank()) {
                        auth.createUserWithEmailAndPassword(email, senha)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userId = auth.currentUser?.uid ?: ""
                                    val usuario = Usuario(
                                        id = userId, // O ID do Firebase Authentication
                                        nome = nome,
                                        nickName = nickName,
                                        email = email,
                                        senha = senha,
                                        filmes = listOf()
                                    )
                                    // Adiciona o usuário ao Firestore
                                    usuarioDAO.adicionar(usuario) { usuarioAdicionado ->
                                        if (usuarioAdicionado.id.isNotEmpty()) {
                                            mensagemSucesso = "Usuário cadastrado com sucesso!"
                                            onSignUpClick()
                                        } else {
                                            mensagemErro = "Erro ao cadastrar usuário no Firestore!"
                                        }
                                    }
                                } else {
                                    mensagemErro = task.exception?.message ?: "Erro ao cadastrar no Authentication"
                                }
                            }
                    } else {
                        mensagemErro = "Por favor, preencha todos os campos!"
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00186F),
                    contentColor = Color.White
                ),
                modifier = Modifier.width(280.dp)
            ) {
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

            mensagemSucesso?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
                LaunchedEffect(Unit) {
                    delay(3000)
                    mensagemSucesso = null
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
