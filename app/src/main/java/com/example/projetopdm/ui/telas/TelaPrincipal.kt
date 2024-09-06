package com.example.projetopdm.ui.telas

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.projetopdm.AppConstants
import com.example.projetopdm.R
import com.example.projetopdm.network.Movie
import com.example.projetopdm.network.RetrofitInstance
import com.example.projetopdm.network.Serie
import com.example.projetopdm.network.TmdbMovieResponse
import com.example.projetopdm.network.TmdbSeriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun FilmesPorVirCarousel() {
    var upcomingMovies by remember { mutableStateOf(listOf<Movie>()) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getUpcomingMovies(AppConstants.TMDB_API_KEY).enqueue(object : Callback<TmdbMovieResponse> {
            override fun onResponse(call: Call<TmdbMovieResponse>, response: Response<TmdbMovieResponse>) {
                response.body()?.let {
                    upcomingMovies = it.results
                }
            }

            override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error fetching upcoming movies", t)
            }
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(upcomingMovies) { movie ->
            MovieItem(movie)
        }
    }
}

@Composable
fun SeriesNoArHojeCarousel() {
    var upcomingSeries by remember { mutableStateOf(listOf<Serie>()) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getUpcomingSeries(AppConstants.TMDB_API_KEY).enqueue(object : Callback<TmdbSeriesResponse> {
            override fun onResponse(call: Call<TmdbSeriesResponse>, response: Response<TmdbSeriesResponse>) {
                response.body()?.let {
                    upcomingSeries = it.results
                }
            }

            override fun onFailure(call: Call<TmdbSeriesResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error fetching upcoming series", t)
            }
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(upcomingSeries) { serie ->
            SerieItem(serie)
        }
    }
}

@Composable
fun FilmesBemAvaliadosCarousel() {
    var topRatedMovies by remember { mutableStateOf(listOf<Movie>()) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getTopRatedMovies(AppConstants.TMDB_API_KEY).enqueue(object : Callback<TmdbMovieResponse> {
            override fun onResponse(call: Call<TmdbMovieResponse>, response: Response<TmdbMovieResponse>) {
                response.body()?.let {
                    topRatedMovies = it.results
                }
            }

            override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error fetching top rated movies", t)
            }
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(topRatedMovies) { movie ->
            MovieItem(movie)
        }
    }
}

@Composable
fun SeriesBemAvaliadasCarousel() {
    var topRatedSeries by remember { mutableStateOf(listOf<Serie>()) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getTopRatedSeries(AppConstants.TMDB_API_KEY).enqueue(object : Callback<TmdbSeriesResponse> {
            override fun onResponse(call: Call<TmdbSeriesResponse>, response: Response<TmdbSeriesResponse>) {
                response.body()?.let {
                    topRatedSeries = it.results
                }
            }

            override fun onFailure(call: Call<TmdbSeriesResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error fetching top rated series", t)
            }
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(topRatedSeries) { serie ->
            SerieItem(serie)
        }
    }
}

@Composable
fun FilmesEmCartazCarousel() {
    var movies by remember { mutableStateOf(listOf<Movie>()) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getNowPlayingMovies(AppConstants.TMDB_API_KEY).enqueue(object : Callback<TmdbMovieResponse> {
            override fun onResponse(call: Call<TmdbMovieResponse>, response: Response<TmdbMovieResponse>) {
                response.body()?.let {
                    movies = it.results
                }
            }

            override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error fetching movies in theaters", t)
            }
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie)
        }
    }
}

@Composable
fun SeriesEmExibicaoCarousel() {
    var series by remember { mutableStateOf(listOf<Serie>()) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getOnTheAirSeries(AppConstants.TMDB_API_KEY).enqueue(object : Callback<TmdbSeriesResponse> {
            override fun onResponse(call: Call<TmdbSeriesResponse>, response: Response<TmdbSeriesResponse>) {
                response.body()?.let {
                    series = it.results
                }
            }

            override fun onFailure(call: Call<TmdbSeriesResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error fetching series on the air", t)
            }
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(series) { serie ->
            SerieItem(serie)
        }
    }
}

@Composable
fun FilmesCarousel() {
    var movies by remember { mutableStateOf(listOf<Movie>()) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getPopularMovies(AppConstants.TMDB_API_KEY).enqueue(object :
            Callback<TmdbMovieResponse> {
            override fun onResponse(
                call: Call<TmdbMovieResponse>,
                response: Response<TmdbMovieResponse>
            ) {
                response.body()?.let {
                    movies = it.results
                }
            }

            override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error fetching movies", t)
            }
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie)
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    Box(
        modifier = Modifier
            .size(120.dp, 180.dp)
            .background(Color.Black, shape = RoundedCornerShape(8.dp))
    ) {
        val posterUrl = AppConstants.TMDB_IMAGE_BASE_URL_ORIGINAL + movie.poster_path
        Image(
            painter = rememberAsyncImagePainter(posterUrl),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "Favorito",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .size(24.dp),
            tint = Color.Red
        )
    }
}

@Composable
fun SeriesCarousel() {
    var series by remember { mutableStateOf(listOf<Serie>()) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getPopularSeries(AppConstants.TMDB_API_KEY).enqueue(object : Callback<TmdbSeriesResponse> {
            override fun onResponse(
                call: Call<TmdbSeriesResponse>,
                response: Response<TmdbSeriesResponse>
            ) {
                response.body()?.let {
                    series = it.results
                }
            }

            override fun onFailure(call: Call<TmdbSeriesResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error fetching series", t)
            }
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(series) { serie ->
            SerieItem(serie)
        }
    }
}

@Composable
fun SerieItem(serie: Serie) {
    Box(
        modifier = Modifier
            .size(120.dp, 180.dp)
            .background(Color.Black, shape = RoundedCornerShape(8.dp))
    ) {
        val posterUrl = AppConstants.TMDB_IMAGE_BASE_URL_ORIGINAL + serie.poster_path
        Image(
            painter = rememberAsyncImagePainter(posterUrl),
            contentDescription = serie.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "Favorito",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .size(24.dp),
            tint = Color.LightGray
        )
    }
}

@Composable
fun TelaPrincipal(modifier: Modifier = Modifier, userId: String, onLogoffClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Filmes Populares",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        FilmesCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Series Populares",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        SeriesCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Filmes Bem Avaliados",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        FilmesBemAvaliadosCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Séries Bem Avaliadas",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        SeriesBemAvaliadasCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Filmes Em Cartaz",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        FilmesEmCartazCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Series Em Exibição",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        SeriesEmExibicaoCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Filmes Por Vir",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        FilmesPorVirCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Séries No Ar Hoje",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        SeriesNoArHojeCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.weight(1f))
    }
}
