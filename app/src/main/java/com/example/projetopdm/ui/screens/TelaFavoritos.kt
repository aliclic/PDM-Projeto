package com.example.projetopdm.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.projetopdm.model.Movie
import com.example.projetopdm.model.dados.ListaFilmes
import com.example.projetopdm.network.RetrofitInstance
import com.example.projetopdm.ui.carousels.CarouselGenerico
import com.example.projetopdm.ui.modals.MovieDetailsModal
import com.example.projetopdm.model.dados.UsuarioDAO
import com.example.projetopdm.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material.icons.filled.Search

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TelaFavoritos(
    userId: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var listaFilmes by remember { mutableStateOf<List<ListaFilmes>>(emptyList()) }
    var filmesDetalhados by remember { mutableStateOf<Map<String, List<Movie>>>(emptyMap()) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showAddListDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val usuarioDAO = UsuarioDAO()
    val tmdbService = remember { RetrofitInstance.api }

    // Carregar as listas e os filmes favoritos do usuário
    LaunchedEffect(userId) {
        usuarioDAO.getUserMovieLists(userId) { listas ->
            listaFilmes = listas
            launch {
                filmesDetalhados = withContext(Dispatchers.IO) {
                    listas.map { lista ->
                        val movieDetails = lista.filmes.map { id ->
                            async {
                                try {
                                    tmdbService.getMovieById(id, AppConstants.TMDB_API_KEY)
                                } catch (e: Exception) {
                                    Log.e("API_ERROR", "Erro ao buscar detalhes do filme com ID: $id", e)
                                    null
                                }
                            }
                        }.awaitAll().filterNotNull()

                        (lista.titulo ?: "Título Desconhecido") to movieDetails
                    }.toMap()
                }
            }
        }
    }

    fun refreshFavorites() {
        usuarioDAO.getUserMovieLists(userId) { listas ->
            listaFilmes = listas
            // Você pode adicionar lógica adicional aqui se necessário
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            // Título da tela
            Text(
                text = "Minhas Listas Favoritas",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            // Campo de busca
            BasicTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            modifier = Modifier.padding(end = 8.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                        Box(Modifier.weight(1f)) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Buscar lista...",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(listaFilmes) { lista ->
                        Column {
                            // Título da lista de filmes
                            Text(
                                text = lista.titulo ?: "Título Desconhecido",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            // Carrossel dos filmes detalhados dessa lista
                            filmesDetalhados[lista.titulo]?.let { filmes ->
                                CarouselGenerico(filmes, navController)
                            }
                        }
                    }
                }
            }
        }

        // Exibir detalhes do filme em um modal (se necessário)
        selectedMovie?.let { movie ->
            MovieDetailsModal(movie) {
                selectedMovie = null
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

@Composable
fun MovieItem(movie: Movie, onMovieClick: (Movie) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onMovieClick(movie) }  // Usa o callback de clique para navegação
            .padding(8.dp)
    ) {
        Text(text = movie.title ?: "Título não disponível")
        Text(text = "Lançamento: ${movie.release_date}")
    }
}
