package com.example.projetopdm.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projetopdm.R
import com.example.projetopdm.model.dados.UsuarioDAO
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

@Composable
fun TelaPerfil(
    userId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    activity: Activity
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nickName by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var originalNome by remember { mutableStateOf("") }
    var originalNickName by remember { mutableStateOf("") }
    var originalEmail by remember { mutableStateOf("") }
    var originalSenha by remember { mutableStateOf("") }
    var isModified by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

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
        isModified = nome != originalNome || nickName != originalNickName || senha != originalSenha
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.padding(50.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(120.dp) // Tamanho quadrado
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture), // Substitua pelo nome do arquivo da imagem
                contentDescription = "Foto de Perfil",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape) // Mantém a imagem circular
            )

            IconButton(
                onClick = {
                    openImagePicker(activity)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Posiciona o botão no canto inferior direito
                    .size(40.dp) // Tamanho do botão
                    .background(Color.White, shape = CircleShape) // Fundo branco com formato circular
                    .padding(8.dp) // Espaçamento interno
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar Foto",
                    tint = Color.Black // Ícone preto
                )
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = "Olá $nickName!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "$email",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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

        TextField(
            value = senha,
            onValueChange = {
                senha = it
                verificarModificacoes()
            },
            label = { Text("Senha") },
            visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (senhaVisivel)
                    Icons.Filled.Close
                else
                    Icons.Filled.Lock

                Icon(
                    imageVector = image,
                    contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha",
                    modifier = Modifier.clickable { senhaVisivel = !senhaVisivel }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

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
        Spacer(modifier = Modifier.padding(16.dp))

        if (isModified) {
            Button(
                onClick = {
                    if (senha.length < 6) {
                        mensagemErro = "A senha deve ter no mínimo 6 caracteres!"
                    } else {
                        val novosDados = mapOf(
                            "nome" to nome,
                            "nickName" to nickName,
                            "senha" to senha
                        )
                        usuarioDAO.atualizarUsuario(currentUserId, novosDados, novaSenha = senha, senhaAtual = originalSenha) { sucesso ->
                            if (sucesso) {
                                originalNome = nome
                                originalNickName = nickName
                                originalSenha = senha
                                isModified = false
                                successMessage = "Perfil atualizado com sucesso!"
                                showSuccessDialog = true
                            } else {
                                mensagemErro = "Erro ao atualizar o perfil!"
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Atualizar")
            }

        } else {
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    onBackClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sair")
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Deletar Conta")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmação") },
                text = { Text("Tem certeza que deseja deletar sua conta? Esta ação não pode ser desfeita.") },
                confirmButton = {
                    Button(
                        onClick = {
                            usuarioDAO.deletarUsuario(currentUserId) { sucesso ->
                                if (sucesso) {
                                    onBackClick() // Volta para a tela de login ou outra tela após deletar a conta
                                } else {
                                    mensagemErro = "Erro ao deletar conta!"
                                }
                            }
                            showDialog = false
                        }
                    ) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Sucesso") },
                text = { Text(successMessage) },
                confirmButton = {
                    Button(onClick = { showSuccessDialog = false }) {
                        Text("Ok")
                    }
                }
            )
        }
    }
}

// Função para abrir o Image Picker
private fun openImagePicker(activity: Activity) {
    val intent = Intent().apply {
        type = "image/*"
        action = Intent.ACTION_GET_CONTENT
    }
    activity.startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), PICK_IMAGE_REQUEST)
}

private const val PICK_IMAGE_REQUEST = 1 // Constante para identificar a solicitação de imagem
