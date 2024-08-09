package com.example.projetopdm.ui.telas

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetopdm.R

@Composable
fun FilmesCarousel() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(10) { index ->
            FilmeItem(imageResource = R.drawable.filme)
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
            SerieItem(imageResource = R.drawable.serie)
        }
    }
}

@Composable
fun FilmeItem(imageResource: Int) {
    Box(
        modifier = Modifier
            .size(120.dp, 180.dp)
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Filme",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "Favorito",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .size(24.dp),
            tint = Color.Red
        )
    }
}

@Composable
fun SerieItem(imageResource: Int) {
    Box(
        modifier = Modifier
            .size(120.dp, 180.dp)
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Série",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "Favorito",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .size(24.dp),
            tint = Color.LightGray
        )
    }
}


@Composable
fun TelaPrincipal(modifier: Modifier = Modifier, onLogoffClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Filmes",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        FilmesCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Series",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        SeriesCarousel()

        Spacer(modifier = Modifier.weight(1f))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00186F), contentColor = Color.White),
            onClick = { onLogoffClick() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Sair")
        }
    }
}
