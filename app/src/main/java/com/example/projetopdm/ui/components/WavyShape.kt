package com.example.projetopdm.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun WavyShape(
    modifier: Modifier = Modifier,
    topWaveColor: Color = Color.Transparent, // Cor da onda convexa (superior)
    bottomWaveColor: Color = Color.White // Cor da onda côncava (inferior)
) {
    Canvas(modifier = modifier) {
        val waveHeight = 50f
        val waveWidth = size.width
        val waveLength = 1200f // Ajuste conforme necessário

        // Desenhe a onda côncava (inferior) com bottomWaveColor
        val bottomWavePath = Path().apply {
            moveTo(0f, size.height)
            for (x in 0..waveWidth.toInt()) {
                val y = size.height - (waveHeight * sin(x / waveLength * 2 * Math.PI.toFloat()))
                lineTo(x.toFloat(), y)
            }
            lineTo(waveWidth, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(bottomWavePath, bottomWaveColor)

        // Desenhe a onda convexa (superior) com topWaveColor
        val topWavePath = Path().apply {
            moveTo(0f, size.height / 2) // Ajuste o início da onda
            for (x in 0..waveWidth.toInt()) {
                val y = size.height / 2 - (waveHeight * sin(x / waveLength * 2 * Math.PI.toFloat()))
                lineTo(x.toFloat(), y)
            }
            lineTo(waveWidth, size.height / 2)
            lineTo(0f, size.height / 2)
            close()
        }
        drawPath(topWavePath, topWaveColor)
    }
}
