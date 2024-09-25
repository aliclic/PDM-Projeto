package com.example.projetopdm.model.dados

import com.google.firebase.firestore.DocumentId

data class Usuario(

    val id: String = "",
    val nickName: String = "",
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val filmes: List<ListaFilmes> = listOf()
)