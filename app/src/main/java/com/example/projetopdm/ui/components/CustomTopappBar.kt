package com.example.projetopdm.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projetopdm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopappBar(title: String) {
    val topBarColor = MaterialTheme.colorScheme.primaryContainer
    val backgroundColor = MaterialTheme.colorScheme.background

    Column {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = topBarColor,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "MyCine Logo",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                    )
                }
            },
            modifier = Modifier
                .background(topBarColor) // Fundo para a TopAppBar
        )
        WavyShape(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.0.dp), // Ajuste a altura da onda conforme necessário
            topWaveColor = backgroundColor, // Cor da onda convexa (superior)
            bottomWaveColor = topBarColor // Cor da onda côncava (inferior)
        )
    }
}


