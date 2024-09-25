package com.example.projetopdm.ui.modals

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
import com.example.projetopdm.model.Serie
import com.example.projetopdm.model.dados.ListaFilmes
import com.example.projetopdm.model.dados.UsuarioDAO
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SerieDetailsModal(serie: Serie?, onDismiss: () -> Unit) {
    if (serie != null) {
        // Estado para controlar se o dropdown está aberto ou não
        val expanded = remember { mutableStateOf(false) }
        val usuarioDAO = UsuarioDAO()
        val listas = remember { mutableStateOf<List<ListaFilmes>>(emptyList()) }
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        LaunchedEffect(userId) {
            userId?.let { id ->
                usuarioDAO.getUserFavorites(id) { filmes ->
                    listas.value = filmes
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
                    Text(text = serie.name, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 8.dp))
                    Text(text = serie.overview)

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
                                    // Exibe a mensagem se não houver listas
                                    DropdownMenuItem(
                                        text = { Text("Você ainda não tem uma lista") },
                                        onClick = { expanded.value = false }
                                    )
                                } else {
                                    listas.value.forEach { lista ->
                                        DropdownMenuItem(
                                            text = { Text(lista.titulo) },
                                            onClick = {
                                                expanded.value = false
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