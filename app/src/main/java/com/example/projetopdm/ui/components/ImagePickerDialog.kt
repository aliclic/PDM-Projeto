package com.example.projetopdm.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ImagePickerDialog(
    onImageSelected: (Uri) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Selecionar Imagem") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Funcionalidade de seleção de imagem fictícia")
                Spacer(modifier = Modifier.height(16.dp))

                // Simulação de uma lista de imagens ou seletor (deve ser substituído por uma implementação real)
                Button(
                    onClick = {
                        selectedUri = Uri.parse("android.resource://com.example.projetopdm/drawable/sample_image")
                    }
                ) {
                    Text("Selecionar Imagem de Teste")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedUri?.let { onImageSelected(it) }
                    onDismiss()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
