package com.example.projetopdm.model.dados

import com.google.firebase.firestore.DocumentId

data class Usuario(

    @DocumentId
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val senha: String = ""
)