package com.example.projetopdm.ui.carousels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projetopdm.model.Movie
import com.example.projetopdm.ui.components.MovieItem
import java.lang.reflect.Modifier
import androidx.compose.foundation.lazy.items

@Composable
fun CarouselGenerico(listFilmes: List<Movie>, navController: NavController) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(listFilmes) { filme ->
            MovieItem(filme) {
                navController.navigate("detalhesFilme/${filme.id}")
            }
        }
    }
}

