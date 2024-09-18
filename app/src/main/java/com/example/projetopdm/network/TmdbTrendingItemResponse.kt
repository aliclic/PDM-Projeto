package com.example.projetopdm.network

import com.example.projetopdm.model.TrendingItem

data class TmdbTrendingItemResponse(
    val page: Int,
    val results: List<TrendingItem>,
    val total_pages: Int,
    val total_results: Int
)