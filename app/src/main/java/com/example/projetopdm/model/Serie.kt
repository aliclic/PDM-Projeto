package com.example.projetopdm.model

class Serie(
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
    val original_name: String,
    val first_air_date: String?,
    val name: String,
    val origin_country: List<String>
) : MediaItem(id, adult, backdrop_path, genre_ids, original_language, overview, popularity, poster_path, vote_average, vote_count)