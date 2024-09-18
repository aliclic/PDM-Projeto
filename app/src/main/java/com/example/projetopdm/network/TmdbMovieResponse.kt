package com.example.projetopdm.network

import com.example.projetopdm.model.Movie

data class TmdbMovieResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)