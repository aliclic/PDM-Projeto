package com.example.projetopdm.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.projetopdm.AppConstants
import com.example.projetopdm.network.Movie
import com.example.projetopdm.network.RetrofitInstance
import com.example.projetopdm.network.TmdbMovieResponse
import com.example.projetopdm.ui.components.MovieItem
import com.example.projetopdm.ui.modals.MovieDetailsModal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun TelaDeBusca(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var isModalVisible by remember { mutableStateOf(false) }

    // Debounce para busca
    var lastSearchQuery by remember { mutableStateOf("") }
    val debounceDelay = 500L // meio segundo de atraso para o debounce

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo de texto para busca com estilo
        BasicTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query

                // Iniciar debounce quando houver alterações
                if (query.isNotEmpty() && query != lastSearchQuery) {
                    lastSearchQuery = query
                    isLoading = true

                    buscarFilmesComDebounce(query, debounceDelay) { filmes ->
                        searchResults = filmes
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                .padding(16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                isLoading = true
                buscarFilmes(searchQuery) { filmes ->
                    searchResults = filmes
                    isLoading = false
                }
            }),
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
                            Text(text = "Buscar filmes...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        }
                        innerTextField()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Indicador de carregamento
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Exibir resultados da busca
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // Número de colunas fixo
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults) { movie ->
                    MovieItem(movie, onClick = {
                        selectedMovie = movie
                        isModalVisible = true
                    })
                }
            }

            if (isModalVisible) {
                MovieDetailsModal(selectedMovie) {
                    isModalVisible = false // Fecha o modal ao clicar no botão de fechar
                }
            }
        }
    }
}

// Função para buscar filmes com debounce
private fun buscarFilmesComDebounce(query: String, delayMs: Long, onMoviesLoaded: (List<Movie>) -> Unit) {
    // Inicia um atraso de debounce
    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
        buscarFilmes(query, onMoviesLoaded)
    }, delayMs)
}

// Função para buscar filmes ao pressionar Enter ou após o debounce
private fun buscarFilmes(query: String, onMoviesLoaded: (List<Movie>) -> Unit) {
    RetrofitInstance.api.searchMovies(AppConstants.TMDB_API_KEY, query, "pt-BR", 1)
        .enqueue(object : Callback<TmdbMovieResponse> {
            override fun onResponse(call: Call<TmdbMovieResponse>, response: Response<TmdbMovieResponse>) {
                response.body()?.let {
                    onMoviesLoaded(it.results)
                }
            }

            override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                Log.e("API_ERROR", "Erro ao buscar filmes", t)
                onMoviesLoaded(emptyList())
            }
        })
}