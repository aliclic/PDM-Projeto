package com.example.projetopdm.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projetopdm.model.Movie
import com.example.projetopdm.model.dados.ListaFilmes
import com.example.projetopdm.network.RetrofitInstance
import com.example.projetopdm.ui.carousels.CarouselGenerico
import com.example.projetopdm.ui.modals.MovieDetailsModal
import com.example.projetopdm.model.dados.UsuarioDAO
import com.example.projetopdm.AppConstants
import com.example.projetopdm.model.Serie
import com.example.projetopdm.network.TmdbMovieResponse
import com.example.projetopdm.network.TmdbSerieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TelaFavoritos(
    userId: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var listaFilmes by remember { mutableStateOf<List<ListaFilmes>>(emptyList()) }
    var showAddListDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var filmesPorLista by remember { mutableStateOf<Map<String, List<Movie>>>(emptyMap()) }

    val usuarioDAO = UsuarioDAO()

    // Carregar as listas de filmes do usuário
    LaunchedEffect(userId) {
        isLoading = true  // Inicia o carregamento
        usuarioDAO.getUserMovieLists(userId) { listas ->
            listaFilmes = listas

            // Carregar detalhes dos filmes de cada lista
            listas.forEach { lista ->
                val filmesIds = lista.filmes  // IDs dos filmes armazenados na lista
                val filmesDetalhes = mutableListOf<Movie>()

                // Para cada filme, fazer a requisição para obter seus detalhes
                filmesIds.forEach { movieId ->
                    usuarioDAO.getMovieById(userId, movieId) { filme -> // Passa userId e movieId
                        if (filme != null) {
                            filmesDetalhes.add(filme)
                        }

                        // Atualiza o estado com a lista de filmes dessa lista específica
                        filmesPorLista = filmesPorLista + (lista.id to filmesDetalhes)
                    }
                }
            }

            isLoading = false  // Finaliza o carregamento
        }
    }

    fun refreshFavorites() {
        usuarioDAO.getUserMovieLists(userId) { listas ->
            listaFilmes = listas
        }
    }

    // Função para remover a lista
    fun removerLista(listaId: String) {
        usuarioDAO.removerListaFilmes(userId, listaId) { success ->
            if (success) {
                // Atualiza a interface após a remoção
                listaFilmes = listaFilmes.filter { it.id != listaId }
                filmesPorLista = filmesPorLista - listaId
            }
        }
    }

    // Função para adicionar um filme à lista
    fun adicionarFilme(listaId: String, filmeId: Int) {
        usuarioDAO.adicionarFilmeNaLista(userId, listaId, filmeId) { success ->
            if (success) {
                // Atualiza a interface após adicionar o filme
                refreshFavorites()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            Text(
                text = "Minhas Listas Favoritas",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(listaFilmes) { lista ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                // Título da lista
                                Text(
                                    text = lista.titulo ?: "Título Desconhecido",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.weight(1f)
                                )

                                // Ícone de deletar ao lado do título
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Remover lista",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable {
                                            removerLista(lista.id)
                                        },
                                    tint = Color.Red
                                )
                            }

                            // Verifica se há filmes para essa lista
                            val filmes = filmesPorLista[lista.id]

                            if (!filmes.isNullOrEmpty()) {
                                // Exibe os filmes em um LazyRow (um carrossel horizontal)
                                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(filmes) { filme ->
                                        Box(
                                            modifier = Modifier
                                                .size(150.dp)  // Define o tamanho do card do filme
                                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                                .clickable {
                                                    // Adiciona um filme à lista ao clicar
                                                    adicionarFilme(lista.id, filme.id) // Altere para filme.id de acordo com a estrutura do objeto
                                                }
                                                .padding(8.dp)
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                // Exibe o título do filme
                                                Text(
                                                    text = filme.title ?: "Sem título",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = "Nenhum filme nessa lista.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Botão flutuante para adicionar nova lista
        FloatingActionButton(
            onClick = { showAddListDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }

        // Caixa de diálogo para adicionar nova lista
        if (showAddListDialog) {
            AlertDialog(
                onDismissRequest = { showAddListDialog = false },
                title = { Text("Nova Lista") },
                text = {
                    Column {
                        TextField(
                            value = newListName,
                            onValueChange = { newListName = it },
                            label = { Text("Nome da Lista") },
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newListName.isNotBlank()) {
                                usuarioDAO.addNewList(userId, newListName) { success ->
                                    if (success) {
                                        refreshFavorites()
                                        newListName = ""
                                        showAddListDialog = false
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Adicionar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showAddListDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}



