package com.example.projetopdm.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaPerfil(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Perfil do Usuário",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 24.sp
        )
        Text(
            text = "Nome: Usuário",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Button(
            onClick = { onBackClick() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar para Login", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
