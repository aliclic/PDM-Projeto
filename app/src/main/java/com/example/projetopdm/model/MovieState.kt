package com.example.projetopdm.model

sealed class MovieState {
    object Loading : MovieState()
    data class Success(val movie: Movie) : MovieState()
    data class Error(val message: String) : MovieState()
}