package com.example.projetopdm.network

import com.example.projetopdm.model.Serie

data class TmdbSerieResponse(
    val page: Int,
    val results: List<Serie>,
    val total_pages: Int,
    val total_results: Int
)
