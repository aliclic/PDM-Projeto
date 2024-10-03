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

private fun <T> loadMovies(
    call: Call<T>,
    onMoviesLoaded: (List<Movie>) -> Unit
) {
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            response.body()?.let {
                // Aqui você pode processar a resposta para extrair a lista de filmes
                // Supondo que a resposta contenha um campo `results` que é uma lista de filmes
                @Suppress("UNCHECKED_CAST")
                val movieList = (it as? TmdbMovieResponse)?.results ?: emptyList()
                onMoviesLoaded(movieList)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e("API_ERROR", "Error fetching movies", t)
            onMoviesLoaded(emptyList())  // Retorna uma lista vazia se falhar
        }
    })
}

private fun <T> loadSeries(
    call: Call<T>,
    onSeriesLoaded: (List<Serie>) -> Unit
) {
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            response.body()?.let {
                // Aqui você pode processar a resposta para extrair a lista de séries
                // Supondo que a resposta contenha um campo `results` que é uma lista de séries
                @Suppress("UNCHECKED_CAST")
                val seriesList = (it as? TmdbSerieResponse)?.results ?: emptyList()
                onSeriesLoaded(seriesList)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e("API_ERROR", "Error fetching series", t)
            onSeriesLoaded(emptyList())  // Retorna uma lista vazia se falhar
        }
    })
}



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
    var filmesPorLista = mutableMapOf<String, List<Movie>>()

    val usuarioDAO = UsuarioDAO()

    // Carregar as listas de filmes do usuário
    LaunchedEffect(userId) {
        isLoading = true  // Inicia o carregamento
        usuarioDAO.getUserMovieLists(userId) { listas ->
            listaFilmes = listas

            listas.forEach { lista ->
                usuarioDAO.getMoviesForList(userId, lista.id) { filmes ->
                    filmesPorLista = (filmesPorLista + (lista.id to filmes)) as MutableMap<String, List<Movie>>
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
                refreshFavorites()  // Atualiza as listas após remoção
            }
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
                                modifier = Modifier.weight(1f)  // Ocupa o espaço disponível
                            )

                            // Ícone de deletar ao lado do título
                            Icon(
                                imageVector = Icons.Filled.Delete,  // Ícone de lixeira
                                contentDescription = "Remover lista",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        removerLista(lista.id)  // Chama a função de remoção
                                    },
                                tint = Color.Red
                            )
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
