package com.example.projetopdm.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilmesCarousel() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(10) { index ->
            FilmeItem(filmeTitulo = "Filme $index")
        }
    }
}

@Composable
fun SeriesCarousel() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(10) { index ->
            SerieItem(serieTitulo = "Série $index")
        }
    }
}

@Composable
fun FilmeItem(filmeTitulo: String) {
    Box(
        modifier = Modifier
            .size(120.dp, 180.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = filmeTitulo,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp)
        )
    }
}

@Composable
fun SerieItem(serieTitulo: String) {
    Box(
        modifier = Modifier
            .size(120.dp, 180.dp)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            text = serieTitulo,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp)
        )
    }
}


@Composable
fun TelaPrincipal(modifier: Modifier = Modifier, onLogoffClick: () -> Unit) {
    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {

        Text(
            text = "Filmes",
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .align(Alignment.Start)
        )
        FilmesCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Series",
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.Start)
        )
        SeriesCarousel()

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = { onLogoffClick() }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Sair")
        }
    }
}



