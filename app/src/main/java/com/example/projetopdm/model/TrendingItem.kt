package com.example.projetopdm.model

data class TrendingItem(
    val id: Int,
    val media_type: String?,  // "movie" ou "tv"
    val backdrop_path: String?,
    val genre_ids: List<Int>?,
    val original_language: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    val original_title: String?,  // Apenas para filmes
    val release_date: String?,    // Apenas para filmes
    val title: String?,           // Apenas para filmes
    val adult: Boolean?,          // Apenas para filmes
    val video: Boolean?,          // Apenas para filmes
    val original_name: String?,   // Apenas para séries
    val first_air_date: String?,  // Apenas para séries
    val name: String?,            // Apenas para séries
    val origin_country: List<String>? // Apenas para séries
) {
    // Função utilitária para saber se é um filme ou série
    fun isMovie() = media_type == "movie"
    fun isSerie() = media_type == "tv"
}
