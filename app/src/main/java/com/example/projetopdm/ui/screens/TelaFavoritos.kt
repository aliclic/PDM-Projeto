package com.example.projetopdm.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projetopdm.model.Movie
import com.example.projetopdm.model.dados.ListaFilmes
import com.example.projetopdm.model.dados.Usuario
import com.example.projetopdm.ui.components.MediaItemCard
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.projetopdm.model.dados.UsuarioDAO
import com.google.firebase.firestore.FieldValue


//fun getUserFavorites(userId: String, onFavoritesLoaded: (List<ListaFilmes>) -> Unit) {
//    val db = FirebaseFirestore.getInstance()
//    db.collection("usuarios").document(userId).get()
//        .addOnSuccessListener { document ->
//            val user = document.toObject(Usuario::class.java)
//            user?.let {
//                onFavoritesLoaded(it.filmes)
//            }
//        }
//        .addOnFailureListener { exception ->
//            Log.e("Firestore", "Error getting user favorites", exception)
//            onFavoritesLoaded(emptyList()) // Retorna uma lista vazia se falhar
//        }
//}
//
//fun addNewList(userId: String, listName: String) {
//    val db = FirebaseFirestore.getInstance()
//    val newList = ListaFilmes(titulo = listName)
//
//    db.collection("usuarios").document(userId).update(
//        "filmes", FieldValue.arrayUnion(newList)
//    )
//        .addOnSuccessListener {
//            Log.d("Firestore", "Nova lista adicionada com sucesso")
//        }
//        .addOnFailureListener { exception ->
//            Log.e("Firestore", "Erro ao adicionar nova lista", exception)
//        }
//}

@Composable
fun TelaFavoritos(
    userId: String,
    modifier: Modifier = Modifier
) {
    var listaFilmes by remember { mutableStateOf<List<ListaFilmes>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") } // Campo de busca
    var showAddListDialog by remember { mutableStateOf(false) } // Controla a exibição do diálogo
    var newListName by remember { mutableStateOf("") } // Nome da nova lista
    val usuarioDAO = UsuarioDAO()

    // Carregar os filmes favoritos do usuário
    LaunchedEffect(userId) {
        usuarioDAO.getUserFavorites(userId) { filmes ->
            listaFilmes = filmes
        }
    }

    fun refreshFavorites() {
        usuarioDAO.getUserFavorites(userId) { filmes ->
            listaFilmes = filmes
        }
    }

    // Filtrar os resultados de acordo com a busca
    val filteredListas = if (searchQuery.isEmpty()) {
        listaFilmes
    } else {
        listaFilmes.filter { it.titulo.contains(searchQuery, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            // Título da tela
            Text(
                text = "Minhas Listas",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                    .align(Alignment.Start)
            )

            // Campo de busca
            BasicTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            modifier = Modifier.padding(end = 8.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                        Box(Modifier.weight(1f)) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Buscar lista...",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Exibição dos filmes favoritos filtrados
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredListas) { category ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Título da categoria
                        Text(
                            text = category.titulo,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                                .align(Alignment.Start)
                        )

                        // Lista de itens favoritos na categoria
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(category.lista) { item ->
                                MediaItemCard(item)
                            }
                        }
                    }
                }
            }
        }

        // Botão flutuante para adicionar nova lista
        FloatingActionButton(
            onClick = { showAddListDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }

        // Caixa de diálogo para adicionar nova lista
        if (showAddListDialog) {
            AlertDialog(
                onDismissRequest = { showAddListDialog = false },
                title = { Text("Nova Lista") },
                text = {
                    Column {
                        Text("Digite o nome da nova lista:")
                        Spacer(modifier = Modifier.height(8.dp))
                        BasicTextField(
                            value = newListName,
                            onValueChange = { newName ->
                                newListName = newName
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                                .padding(8.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newListName.isNotBlank()) {
                                usuarioDAO.addNewList(userId, newListName) { success ->
                                    if (success) {
                                        refreshFavorites()
                                        newListName = ""
                                        showAddListDialog = false
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Adicionar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddListDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}


