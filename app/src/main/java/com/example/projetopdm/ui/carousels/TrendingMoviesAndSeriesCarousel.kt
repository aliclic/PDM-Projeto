package com.example.projetopdm.ui.carousels

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.projetopdm.model.Movie
import com.example.projetopdm.model.Serie
import com.example.projetopdm.model.TrendingItem
import com.example.projetopdm.ui.components.TrendingItem
import com.example.projetopdm.ui.modals.MovieDetailsModal
import com.example.projetopdm.ui.modals.SerieDetailsModal
import com.example.projetopdm.ui.screens.loadPopularSeries
import com.example.projetopdm.ui.screens.loadTrendingMoviesAndSeries

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrendingMoviesAndSeriesCarousel() {
    var trendingItems by remember { mutableStateOf(listOf<TrendingItem>()) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var selectedSerie by remember { mutableStateOf<Serie?>(null) }
    var page by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var isModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        loadTrendingMoviesAndSeries(page) { items ->
            trendingItems = items
            isLoading = false
        }
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(trendingItems) { item ->
            TrendingItem(item, onClick = {
                when (item.media_type) {
                    "movie" -> {
                        selectedMovie = item.toMovie()
                        selectedSerie = null
                    }
                    "tv" -> {
                        selectedSerie = item.toSerie()
                        selectedMovie = null
                    }
                }
                isModalVisible = true
            })
        }

        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LaunchedEffect(trendingItems.size) {
                    isLoading = true
                    page++
                    loadTrendingMoviesAndSeries(page) { newItems ->
                        trendingItems = trendingItems + newItems
                        isLoading = false
                    }
                }
            }
        }
    }

    if (isModalVisible) {
        selectedMovie?.let { movie ->
            MovieDetailsModal(movie) {
                isModalVisible = false
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
        adult = this.adult ?: false,
        backdrop_path = this.backdrop_path,
        genre_ids = this.genre_ids ?: listOf(),
        original_language = this.original_language ?: "Unknown",
        overview = this.overview ?: "",
        popularity = this.popularity ?: 0.0,
        poster_path = this.poster_path,
        vote_average = this.vote_average ?: 0.0,
        vote_count = this.vote_count ?: 0,
        original_title = this.original_title ?: "",
        release_date = this.release_date,
        title = this.title ?: "",
        video = this.video ?: false // Ajustar se o item contiver informações de vídeo
    )
}

fun TrendingItem.toSerie(): Serie {
    return Serie(
        id = this.id,
        adult = this.adult ?: false,
        backdrop_path = this.backdrop_path,
        genre_ids = this.genre_ids ?: listOf(),
        original_language = this.original_language ?: "Unknown",
        overview = this.overview ?: "",
        popularity = this.popularity ?: 0.0,
        poster_path = this.poster_path,
        vote_average = this.vote_average ?: 0.0,
        vote_count = this.vote_count ?: 0,
        original_name = this.original_name ?: "",
        first_air_date = this.first_air_date,
        name = this.name ?: "",
        origin_country = this.origin_country ?: listOf()
    )
}