package com.example.projetopdm.model.dados

data class Usuario(

    val id: String = "",
    val nickName: String = "",
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val filmes: List<ListaFilmes> = listOf(),
    val profileImageUrl: String = ""
)