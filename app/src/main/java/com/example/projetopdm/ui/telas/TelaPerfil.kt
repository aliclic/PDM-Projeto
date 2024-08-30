package com.example.projetopdm.ui.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetopdm.R
import com.example.projetopdm.model.dados.Usuario
import com.example.projetopdm.model.dados.UsuarioDAO
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit


@Composable
fun TelaPerfil(userId: String, modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    val usuarioDAO = UsuarioDAO()

    // Buscar informações do usuário pelo ID
    LaunchedEffect(userId) {
        usuarioDAO.buscarPorId(userId) { usuario ->
            usuario?.let {
                nome = it.nome
                email = it.email
                senha = it.senha
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Perfil do Usuário",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Campo de nome do usuário com ícone de edição
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar Nome",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // Ação para salvar o nome atualizado
                        usuarioDAO.atualizarNome(userId, nome) { sucesso ->
                            if (!sucesso) {
                                mensagemErro = "Erro ao atualizar o nome!"
                            }
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Campo de email do usuário com ícone de edição
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar Email",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // Ação para salvar o email atualizado
                        usuarioDAO.atualizarEmail(userId, email) { sucesso ->
                            if (!sucesso) {
                                mensagemErro = "Erro ao atualizar o email!"
                            }
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Campo de senha do usuário com ícone de edição
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar Senha",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // Ação para salvar a senha atualizada
                        usuarioDAO.atualizarSenha(userId, senha) { sucesso ->
                            if (!sucesso) {
                                mensagemErro = "Erro ao atualizar a senha!"
                            }
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        // Botão de logout
        Button(
            onClick = {
                usuarioDAO.logout { sucesso ->
                    if (sucesso) {
                        onBackClick() // Redireciona para a tela de login
                    } else {
                        mensagemErro = "Erro ao fazer logout!"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sair")
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Botão para deletar a conta
        Button(
            onClick = {
                usuarioDAO.deletarUsuario(userId) { sucesso ->
                    if (sucesso) {
                        onBackClick()  // Redireciona após a conta ser deletada
                    } else {
                        mensagemErro = "Erro ao deletar a conta!"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Deletar Conta")
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
