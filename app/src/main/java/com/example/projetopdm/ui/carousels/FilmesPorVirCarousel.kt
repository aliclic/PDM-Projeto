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
import com.example.projetopdm.ui.components.MovieItem
import com.example.projetopdm.ui.modals.MovieDetailsModal
import com.example.projetopdm.ui.screens.getUpcomingMovies

@Composable
fun FilmesPorVirCarousel() {
    var movies by remember { mutableStateOf(listOf<Movie>()) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var page by remember { mutableStateOf(1) }  // Variável para rastrear a página atual
    var isLoading by remember { mutableStateOf(false) }  // Variável para evitar múltiplas chamadas
    var isModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        getUpcomingMovies(page, onMoviesLoaded = {
            movies = it
            isLoading = false
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie, onClick = {
                selectedMovie = movie
                isModalVisible = true
            })
        }

        // Detecta quando chega ao final da lista e carrega mais
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LaunchedEffect(movies.size) {
                    isLoading = true
                    page++
                    getUpcomingMovies(page) { newMovies ->
                        movies = movies + newMovies
                        isLoading = false
                    }
                }
            }
        }
    }

    // Exibe o modal de detalhes se isModalVisible for true
    if (isModalVisible) {
        MovieDetailsModal(selectedMovie) {
            isModalVisible = false // Fecha o modal ao clicar no botão de fechar
        }
    }
}