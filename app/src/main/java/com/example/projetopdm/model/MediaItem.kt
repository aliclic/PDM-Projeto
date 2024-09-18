package com.example.projetopdm.model

open class MediaItem(
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val original_language: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val vote_average: Double,
    val vote_count: Int
)
