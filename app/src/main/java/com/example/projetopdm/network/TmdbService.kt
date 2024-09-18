package com.example.projetopdm.network

import com.example.projetopdm.model.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET("trending/all/{time_window}")
    fun getTrendingAll(
        @Path("time_window") timeWindow: String,  // Pode ser "day" ou "week"
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbTrendingItemResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbMovieResponse>

    @GET("tv/popular")
    fun getPopularSeries(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbSerieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbMovieResponse>

    @GET("tv/top_rated")
    fun getTopRatedSeries(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbSerieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbMovieResponse>

    @GET("tv/on_the_air")
    fun getOnTheAirSeries(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbSerieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbMovieResponse>

    // GetById
    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "pt-BR"
    ): Movie

    // Searchs
    @GET("search/movie")
    fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "pt-BR",
        @Query("page") page: Int = 1
    ): Call<TmdbMovieResponse>

    @GET("search/tv")
    fun searchTVShows(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "pt-BR",
        @Query("page") page: Int = 1
    ): Call<TmdbSerieResponse>
}