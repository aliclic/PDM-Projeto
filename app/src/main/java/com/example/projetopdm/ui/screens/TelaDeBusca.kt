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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projetopdm.AppConstants
import com.example.projetopdm.model.Movie
import com.example.projetopdm.model.MediaItem
import com.example.projetopdm.model.Serie
import com.example.projetopdm.network.RetrofitInstance
import com.example.projetopdm.network.TmdbMovieResponse
import com.example.projetopdm.network.TmdbSerieResponse
import com.example.projetopdm.ui.components.MovieItem
import com.example.projetopdm.ui.components.SerieItem
import com.example.projetopdm.ui.modals.MovieDetailsModal
import com.example.projetopdm.ui.modals.SerieDetailsModal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun TelaDeBusca(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<MediaItem>>(emptyList()) } // Lista de resultados como MediaItem
    var currentPage by remember { mutableStateOf(1) }  // Página atual
    var isLoadingMore by remember { mutableStateOf(false) } // Indica se mais itens estão sendo carregados
    var selectedItem: MediaItem? by remember { mutableStateOf(null) }
    var isModalVisible by remember { mutableStateOf(false) }
    val debounceDelay = 500L

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo de busca
        BasicTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                currentPage = 1 // Reinicia a página quando a busca muda
                isLoading = true

                buscarFilmesESeriesComDebounce(query, debounceDelay, currentPage) { resultados ->
                    searchResults = resultados
                    isLoading = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
                            Text(text = "Buscar filmes ou séries...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
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
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults) { item ->
                    when (item) {
                        is Movie -> MovieItem(item, onClick = {
                            selectedItem = item
                            isModalVisible = true
                        })
                        is Serie -> SerieItem(item, onClick = {
                            selectedItem = item
                            isModalVisible = true
                        })
                    }
                }

                if (isLoadingMore) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }

            // Detecta quando o usuário chega ao final da lista para carregar mais itens
            val lastVisibleIndex = searchResults.size - 1
            if (lastVisibleIndex >= 0 && !isLoadingMore && lastVisibleIndex == searchResults.size - 1) {
                currentPage += 1
                isLoadingMore = true
                buscarFilmesESeries(searchQuery, currentPage) { novosResultados ->
                    searchResults = searchResults + novosResultados
                    isLoadingMore = false
                }
            }
        }
    }

    // Mostrar o modal correspondente ao item selecionado
    if (isModalVisible) {
        selectedItem?.let { item ->
            when (item) {
                is Movie -> {
                    MovieDetailsModal(item) {
                        isModalVisible = false // Fecha o modal ao clicar no botão de fechar
                    }
                }
                is Serie -> {
                    SerieDetailsModal(item) {
                        isModalVisible = false // Fecha o modal ao clicar no botão de fechar
                    }
                }
            }
        }
    }
}

private fun buscarFilmesESeriesComDebounce(query: String, delayMs: Long, page: Int, onResultsLoaded: (List<MediaItem>) -> Unit) {
    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
        buscarFilmesESeries(query, page, onResultsLoaded)
    }, delayMs)
}

private fun buscarFilmesESeries(query: String, page: Int, onResultsLoaded: (List<MediaItem>) -> Unit) {
    val filmesCall = RetrofitInstance.api.searchMovies(AppConstants.TMDB_API_KEY, query, "pt-BR", page)
    val seriesCall = RetrofitInstance.api.searchTVShows(AppConstants.TMDB_API_KEY, query, "pt-BR", page)

    val mediaItems = mutableListOf<MediaItem>()

    // Chamada para buscar filmes
    filmesCall.enqueue(object : Callback<TmdbMovieResponse> {
        override fun onResponse(call: Call<TmdbMovieResponse>, response: Response<TmdbMovieResponse>) {
            response.body()?.let {
                mediaItems.addAll(it.results) // Adiciona filmes
            }

            // Após buscar os filmes, buscar as séries
            seriesCall.enqueue(object : Callback<TmdbSerieResponse> {
                override fun onResponse(call: Call<TmdbSerieResponse>, response: Response<TmdbSerieResponse>) {
                    response.body()?.let {
                        mediaItems.addAll(it.results) // Adiciona séries
                    }
                    onResultsLoaded(mediaItems)
                }

                override fun onFailure(call: Call<TmdbSerieResponse>, t: Throwable) {
                    Log.e("API_ERROR", "Erro ao buscar séries", t)
                    onResultsLoaded(mediaItems)
                }
            })
        }

        override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
            Log.e("API_ERROR", "Erro ao buscar filmes", t)
            onResultsLoaded(emptyList())
        }
    })
}
