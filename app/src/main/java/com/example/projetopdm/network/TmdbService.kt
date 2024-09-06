package com.example.projetopdm.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbMovieResponse>

    @GET("tv/popular")
    fun getPopularSeries(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbSeriesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String
    ): Call<TmdbMovieResponse>

    @GET("tv/top_rated")
    fun getTopRatedSeries(
        @Query("api_key") apiKey: String
    ): Call<TmdbSeriesResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
    ): Call<TmdbMovieResponse>

    @GET("tv/on_the_air")
    fun getOnTheAirSeries(
        @Query("api_key") apiKey: String,
    ): Call<TmdbSeriesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String
    ): Call<TmdbMovieResponse>

    @GET("tv/airing_today")
    fun getUpcomingSeries(
        @Query("api_key") apiKey: String,
    ): Call<TmdbSeriesResponse>
}