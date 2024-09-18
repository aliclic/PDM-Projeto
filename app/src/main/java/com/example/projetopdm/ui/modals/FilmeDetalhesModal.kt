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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.projetopdm.model.Movie

@Composable
fun MovieDetailsModal(movie: Movie?, onDismiss: () -> Unit) {
    if (movie != null) {
        // Estado para controlar se o dropdown está aberto ou não
        val expanded = remember { mutableStateOf(false) }

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
                    // Exibe o título do filme
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Exibe a descrição
                    Text(text = movie.overview)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Botão para fechar o modal
                        Button(onClick = { onDismiss() }) {
                            Text(text = "Fechar")
                        }

                        // Ícone de lista com dropdown
                        Column {
                            IconButton(onClick = { expanded.value = !expanded.value }) {
                                Icon(
                                    imageVector = Icons.Default.List, // Ícone de lista padrão do Material
                                    contentDescription = "Lista"
                                )
                            }
                            // Dropdown que aparece ao clicar no ícone
                            DropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Ação 1") },
                                    onClick = {
                                        // Fechar o dropdown após a ação
                                        expanded.value = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Ação 2") },
                                    onClick = {
                                        expanded.value = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Ação 3") },
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