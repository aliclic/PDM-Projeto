package com.example.projetopdm.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projetopdm.model.dados.UsuarioDAO
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.imePadding
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TelaPerfil(userId: String, modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nickName by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var originalNome by remember { mutableStateOf("") }
    var originalNickName by remember { mutableStateOf("") }
    var originalEmail by remember { mutableStateOf("") }
    var originalSenha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }
    var isModified by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    val usuarioDAO = UsuarioDAO()

    LaunchedEffect(currentUserId) {
        usuarioDAO.buscarPorId(currentUserId) { usuario ->
            usuario?.let {
                nome = it.nome
                email = it.email
                nickName = it.nickName
                senha = it.senha

                originalNome = it.nome
                originalNickName = it.nickName
                originalEmail = it.email
                originalSenha = it.senha
            }
        }
    }

    fun verificarModificacoes() {
        isModified = nome != originalNome || nickName != originalNickName || email != originalEmail || senha != originalSenha
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Spacer(modifier = Modifier.padding(50.dp))

        // Círculo para simular a foto de perfil
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color = Color.Gray) // Cor cinza para indicar o lugar da foto
            }
            Text(
                text = "Foto",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = "Olá $nickName !",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo para o novo nome do usuário
        TextField(
            value = nome,
            onValueChange = {
                nome = it
                verificarModificacoes()
            },
            label = { Text("Novo Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Campo de NickName
        TextField(
            value = nickName,
            onValueChange = {
                nickName = it
                verificarModificacoes()
            },
            label = { Text("NickName") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Campo de email
        TextField(
            value = email,
            onValueChange = {
                email = it
                verificarModificacoes()
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Campo de senha
        TextField(
            value = senha,
            onValueChange = {
                senha = it
                verificarModificacoes()
            },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(16.dp))

        // Botão "Atualizar" ou "Sair"
        if (isModified) {
            Button(
                onClick = {
                    val novosDados = mapOf(
                        "nome" to nome,
                        "nickName" to nickName,
                        "email" to email,
                        "senha" to senha
                    )
                    usuarioDAO.atualizarUsuario(currentUserId, novosDados, novoEmail = email, novaSenha = senha) { sucesso ->
                        if (sucesso) {
                            originalNome = nome
                            originalNickName = nickName
                            originalEmail = email
                            originalSenha = senha
                            isModified = false
                            successMessage = "Perfil atualizado com sucesso!"
                            showSuccessDialog = true
                        } else {
                            mensagemErro = "Erro ao atualizar o perfil!"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Atualizar")
            }
        } else {
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sair")
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))

        // Botão para deletar a conta
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Deletar Conta")
        }

        Spacer(modifier = Modifier.padding(100.dp))

        // Diálogo de confirmação
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmação") },
                text = { Text("Tem certeza de que deseja deletar sua conta?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            usuarioDAO.deletarUsuario(currentUserId) { sucesso ->
                                if (sucesso) {
                                    onBackClick()
                                } else {
                                    mensagemErro = "Erro ao deletar a conta!"
                                }
                            }
                        }
                    ) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Não")
                    }
                }
            )
        }

        // Diálogo de sucesso para atualizar ou deletar
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Sucesso") },
                text = { Text(successMessage) },
                confirmButton = {
                    Button(onClick = {
                        showSuccessDialog = false
                    }) {
                        Text("OK")
                    }
                }
            )
        }

        // Exibição de mensagens de erro
        mensagemErro?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(3000)
                mensagemErro = null
            }
        }
    }
}
