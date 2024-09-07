package com.example.projetopdm.network

data class TrendingItem(
    val id: Int,
    val name: String?,
    val title: String?,
    val media_type: String,  // "movie" para filmes, "tv" para séries
    val overview: String,
    val poster_path: String?
)