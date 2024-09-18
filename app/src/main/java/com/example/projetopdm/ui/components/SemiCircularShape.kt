package com.example.projetopdm.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
fun SemiCircularShape(
    modifier: Modifier = Modifier,
    leftSemiCircleColor: Color = Color.Transparent, // Cor do semicírculo da esquerda
    rightSemiCircleColor: Color = Color.White // Cor do semicírculo da direita
) {
    Canvas(modifier = modifier) {
        val radius = size.height / 2 // Raio dos semicírculos
        val centerX = size.width / 2 // Centro da tela no eixo X

        // Desenhe o semicírculo da esquerda
        val leftSemiCirclePath = Path().apply {
            moveTo(0f, size.height)
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = size.height - radius * 2,
                    right = radius * 2,
                    bottom = size.height
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            lineTo(0f, size.height)
            close()
        }
        drawPath(leftSemiCirclePath, leftSemiCircleColor)

        // Desenhe o semicírculo da direita
        val rightSemiCirclePath = Path().apply {
            moveTo(size.width, size.height)
            arcTo(
                rect = Rect(
                    left = size.width - radius * 2,
                    top = size.height - radius * 2,
                    right = size.width,
                    bottom = size.height
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            lineTo(size.width, size.height)
            close()
        }
        drawPath(rightSemiCirclePath, rightSemiCircleColor)
    }
}
