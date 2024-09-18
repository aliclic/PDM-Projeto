package com.example.projetopdm.model

class Movie(
    id: Int,
    adult: Boolean,
    backdrop_path: String?,
    genre_ids: List<Int>,
    original_language: String,
    overview: String,
    popularity: Double,
    poster_path: String?,
    vote_average: Double,
    vote_count: Int,
    val original_title: String,
    val release_date: String?,
    val title: String,
    val video: Boolean
) : MediaItem(id, adult, backdrop_path, genre_ids, original_language, overview, popularity, poster_path, vote_average, vote_count)
