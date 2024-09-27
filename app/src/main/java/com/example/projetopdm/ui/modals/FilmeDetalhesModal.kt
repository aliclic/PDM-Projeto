package com.example.projetopdm.ui.modals

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.projetopdm.model.Movie
import com.example.projetopdm.model.dados.ListaFilmes
import com.example.projetopdm.model.dados.UsuarioDAO

import com.google.firebase.auth.FirebaseAuth


@Composable
fun MovieDetailsModal(movie: Movie?, onDismiss: () -> Unit) {
    if (movie != null) {
        val expanded = remember { mutableStateOf(false) }
        val usuarioDAO = UsuarioDAO()
        val listas = remember { mutableStateOf<List<ListaFilmes>>(emptyList()) }
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Carrega as listas de filmes favoritas do usuário
        LaunchedEffect(userId) {
            userId?.let { id ->
                usuarioDAO.getUserMovieLists(id) { listasFilmes ->
                    listas.value = listasFilmes // Atualiza o estado com as listas do usuário
                }
            }
        }


        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = movie.title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 8.dp))
                    Text(text = movie.overview)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(onClick = { onDismiss() }) {
                            Text(text = "Fechar")
                        }

                        Column {
                            IconButton(onClick = {
                                expanded.value = !expanded.value
                            }) {
                                Icon(imageVector = Icons.Default.List, contentDescription = "Lista")
                            }

                            DropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false }
                            ) {
                                if (listas.value.isEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text("Você ainda não tem uma lista") },
                                        onClick = { expanded.value = false }
                                    )
                                } else {
                                    listas.value.forEach { lista ->
                                        DropdownMenuItem(
                                            text = { Text(lista.titulo ?: "Sem título") },
                                            onClick = {
                                                expanded.value = false
                                                try {
                                                    userId?.let { id ->
                                                        usuarioDAO.adicionarFilmeNaLista(id, lista.id, movie.id) { sucesso ->
                                                            if (sucesso) {
                                                                Log.d("Firestore", "Filme adicionado com sucesso à lista ${lista.titulo}")
                                                            } else {
                                                                Log.e("Firestore", "Erro ao adicionar filme à lista ${lista.titulo}")
                                                            }
                                                        }
                                                    }
                                                } catch (e: Exception) {
                                                    Log.e("ModalError", "Erro ao adicionar filme à lista", e)
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}


