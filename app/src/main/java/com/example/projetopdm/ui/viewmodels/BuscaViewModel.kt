package com.example.projetopdm.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetopdm.AppConstants
import com.example.projetopdm.model.MediaItem
import com.example.projetopdm.network.RetrofitInstance
import com.example.projetopdm.network.TmdbMovieResponse
import com.example.projetopdm.network.TmdbSerieResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscaViewModel : ViewModel() {

    var searchQuery = mutableStateOf("")
        private set

    var isLoading = mutableStateOf(false)
        private set

    var searchResults = mutableStateOf<List<MediaItem>>(emptyList())
        private set

    var currentPage = mutableStateOf(1)
        private set

    var isLoadingMore = mutableStateOf(false)
        private set

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
        currentPage.value = 1
        buscarFilmesESeries(query, currentPage.value)
    }

    private fun buscarFilmesESeries(query: String, page: Int) {
        isLoading.value = true

        val filmesCall = RetrofitInstance.api.searchMovies(AppConstants.TMDB_API_KEY, query, "pt-BR", page)
        val seriesCall = RetrofitInstance.api.searchTVShows(AppConstants.TMDB_API_KEY, query, "pt-BR", page)

        val mediaItems = mutableListOf<MediaItem>()

        filmesCall.enqueue(object : Callback<TmdbMovieResponse> {
            override fun onResponse(call: Call<TmdbMovieResponse>, response: Response<TmdbMovieResponse>) {
                response.body()?.let {
                    mediaItems.addAll(it.results) // Adiciona filmes
                }

                // Após buscar os filmes, buscar as séries
                seriesCall.enqueue(object : Callback<TmdbSerieResponse> {
                    override fun onResponse(call: Call<TmdbSerieResponse>, response: Response<TmdbSerieResponse>) {
                        response.body()?.let {
                            mediaItems.addAll(it.results) // Adiciona séries
                        }
                        searchResults.value = mediaItems
                        isLoading.value = false
                    }

                    override fun onFailure(call: Call<TmdbSerieResponse>, t: Throwable) {
                        isLoading.value = false
                    }
                })
            }

            override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                isLoading.value = false
            }
        })
    }

    fun carregarMaisResultados() {
        if (!isLoadingMore.value && searchQuery.value.isNotEmpty()) {
            currentPage.value += 1
            isLoadingMore.value = true

            buscarFilmesESeries(searchQuery.value, currentPage.value)
            isLoadingMore.value = false
        }
    }
}