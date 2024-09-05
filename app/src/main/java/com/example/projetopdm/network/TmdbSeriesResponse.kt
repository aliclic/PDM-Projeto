package com.example.projetopdm.network

data class TmdbSeriesResponse(
    val page: Int,
    val results: List<Serie>,
    val total_pages: Int,
    val total_results: Int
)