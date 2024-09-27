package com.example.projetopdm.model.dados;
import com.example.projetopdm.model.Movie
import com.google.firebase.firestore.DocumentId

data class ListaFilmes(

    @DocumentId
    val id: String = "",
    val titulo: String? = "Lista",
    val filmes: List<Int> = emptyList()
)
