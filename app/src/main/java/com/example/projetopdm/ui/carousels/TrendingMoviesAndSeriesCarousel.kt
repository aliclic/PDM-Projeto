package com.example.projetopdm.ui.carousels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projetopdm.network.Movie
import com.example.projetopdm.network.Serie
import com.example.projetopdm.network.TrendingItem
import com.example.projetopdm.ui.components.TrendingItem
import com.example.projetopdm.ui.modals.MovieDetailsModal
import com.example.projetopdm.ui.modals.SerieDetailsModal
import com.example.projetopdm.ui.screens.loadPopularSeries
import com.example.projetopdm.ui.screens.loadTrendingMoviesAndSeries

@Composable
fun TrendingMoviesAndSeriesCarousel() {
    var trendingItems by remember { mutableStateOf(listOf<TrendingItem>()) }
    var selectedItem by remember { mutableStateOf<TrendingItem?>(null) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var selectedSerie by remember { mutableStateOf<Serie?>(null) }
    var page by remember { mutableStateOf(1) }  // Variável para rastrear a página atual
    var isLoading by remember { mutableStateOf(false) }  // Variável para evitar múltiplas chamadas
    var isModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        loadTrendingMoviesAndSeries(page, onTrendingLoaded = {
            trendingItems = it
            isLoading = false
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(trendingItems) { item ->
            TrendingItem(item, onClick = {
                when (item.media_type) {
                    "movie" -> {
                        selectedMovie = item.toMovie()  // Converte para Movie
                        selectedSerie = null  // Limpa seleção de série
                    }
                    "tv" -> {
                        selectedSerie = item.toSerie()  // Converte para Serie
                        selectedMovie = null  // Limpa seleção de filme
                    }
                }
                isModalVisible = true
            })
        }

        // Detecta quando chega ao final da lista e carrega mais
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LaunchedEffect(trendingItems.size) {
                    isLoading = true
                    page++
                    loadTrendingMoviesAndSeries(page) { newSeries ->
                        trendingItems = trendingItems + newSeries
                        isLoading = false
                    }
                }
            }
        }
    }

    // Exibe o modal dependendo do tipo selecionado
    if (isModalVisible) {
        selectedMovie?.let { movie ->
            MovieDetailsModal(movie) {
                isModalVisible = false // Fecha o modal
            }
        }

        selectedSerie?.let { serie ->
            SerieDetailsModal(serie) {
                isModalVisible = false
            }
        }
    }
}

fun TrendingItem.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title ?: "",
        overview = this.overview ?: "",
        poster_path = this.poster_path ?: ""
    )
}

fun TrendingItem.toSerie(): Serie {
    return Serie(
        id = this.id,
        name = this.name ?: "",
        overview = this.overview ?: "",
        poster_path = this.poster_path ?: ""
    )
}