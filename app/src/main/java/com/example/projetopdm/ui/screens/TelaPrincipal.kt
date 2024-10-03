package com.example.projetopdm.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.projetopdm.AppConstants
import com.example.projetopdm.R
import com.example.projetopdm.model.Movie
import com.example.projetopdm.model.Serie
import com.example.projetopdm.model.TrendingItem
import com.example.projetopdm.network.RetrofitInstance
import com.example.projetopdm.network.TmdbMovieResponse
import com.example.projetopdm.network.TmdbSerieResponse
import com.example.projetopdm.network.TmdbTrendingItemResponse
import com.example.projetopdm.ui.carousels.FilmesBemAvaliadosCarousel
import com.example.projetopdm.ui.carousels.FilmesEmCartazCarousel
import com.example.projetopdm.ui.carousels.FilmesPopularesCarousel
import com.example.projetopdm.ui.carousels.FilmesPorVirCarousel
import com.example.projetopdm.ui.carousels.SeriesBemAvaliadasCarousel
import com.example.projetopdm.ui.carousels.SeriesEmExibicaoCarousel
import com.example.projetopdm.ui.carousels.SeriesPopularesCarousel
import com.example.projetopdm.ui.carousels.TrendingMoviesAndSeriesCarousel
import com.example.projetopdm.ui.modals.MovieDetailsModal
import com.example.projetopdm.util.formatDateToBrazilian
//import com.example.projetopdm.ui.carousels.TrendingMoviesAndSeriesCarousel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sqrt
import kotlin.random.Random

// Função para carregar filmes
private fun <T> loadMovies(
    call: Call<T>,
    onMoviesLoaded: (List<Movie>) -> Unit
) {
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            response.body()?.let {
                // Aqui você pode processar a resposta para extrair a lista de filmes
                // Supondo que a resposta contenha um campo `results` que é uma lista de filmes
                @Suppress("UNCHECKED_CAST")
                val movieList = (it as? TmdbMovieResponse)?.results ?: emptyList()
                onMoviesLoaded(movieList)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e("API_ERROR", "Error fetching movies", t)
            onMoviesLoaded(emptyList())  // Retorna uma lista vazia se falhar
        }
    })
}

// Função para carregar series
private fun <T> loadSeries(
    call: Call<T>,
    onSeriesLoaded: (List<Serie>) -> Unit
) {
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            response.body()?.let {
                // Aqui você pode processar a resposta para extrair a lista de séries
                // Supondo que a resposta contenha um campo `results` que é uma lista de séries
                @Suppress("UNCHECKED_CAST")
                val seriesList = (it as? TmdbSerieResponse)?.results ?: emptyList()
                onSeriesLoaded(seriesList)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e("API_ERROR", "Error fetching series", t)
            onSeriesLoaded(emptyList())  // Retorna uma lista vazia se falhar
        }
    })
}

// Função para carregar apenas filmes e séries trending
fun loadTrendingMoviesAndSeries(page: Int, onTrendingLoaded: (List<TrendingItem>) -> Unit) {
    RetrofitInstance.api.getTrendingAll("week", AppConstants.TMDB_API_KEY, "pt-BR", page).enqueue(object :
        Callback<TmdbTrendingItemResponse> {
        override fun onResponse(
            call: Call<TmdbTrendingItemResponse>,
            response: Response<TmdbTrendingItemResponse>
        ) {
            response.body()?.let { trendingResponse ->
                val filteredItems = trendingResponse.results.filter {
                    it.media_type == "movie" || it.media_type == "tv"
                }
                onTrendingLoaded(filteredItems)
            }
        }

        override fun onFailure(call: Call<TmdbTrendingItemResponse>, t: Throwable) {
            Log.e("API_ERROR", "Error fetching trending movies and series", t)
            onTrendingLoaded(emptyList())  // Retorna uma lista vazia se falhar
        }
    })
}

// Carrega filmes populares
fun loadPopularMovies(page: Int, onMoviesLoaded: (List<Movie>) -> Unit) {
    val call = RetrofitInstance.api.getPopularMovies(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadMovies(call, onMoviesLoaded)
}

// Carrega filmes mais bem avaliados
fun loadTopRatedMovies(page: Int, onMoviesLoaded: (List<Movie>) -> Unit) {
    val call = RetrofitInstance.api.getTopRatedMovies(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadMovies(call, onMoviesLoaded)
}

// Carrega filmes em cartaz
fun loadNowPlayingMovies(page: Int, onMoviesLoaded: (List<Movie>) -> Unit) {
    val call = RetrofitInstance.api.getNowPlayingMovies(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadMovies(call, onMoviesLoaded)
}

// Carrega filmes por vir
fun getUpcomingMovies(page: Int, onMoviesLoaded: (List<Movie>) -> Unit) {
    val call = RetrofitInstance.api.getUpcomingMovies(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadMovies(call, onMoviesLoaded)
}

// Carrega séries populares
fun loadPopularSeries(page: Int, onSeriesLoaded: (List<Serie>) -> Unit) {
    val call = RetrofitInstance.api.getPopularSeries(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadSeries(call, onSeriesLoaded)
}

// Carrega séries mais bem avaliadas
fun loadTopRatedSeries(page: Int, onSeriesLoaded: (List<Serie>) -> Unit) {
    val call = RetrofitInstance.api.getTopRatedSeries(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadSeries(call, onSeriesLoaded)
}

// Carrega séries em exibição
fun loadOnTheAirSeries(page: Int, onSeriesLoaded: (List<Serie>) -> Unit) {
    val call = RetrofitInstance.api.getOnTheAirSeries(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadSeries(call, onSeriesLoaded)
}


@Composable
fun TelaPrincipal(
    modifier: Modifier = Modifier,
    userId: String,
    onLogoffClick: () -> Unit,
    navController: NavController
) {

    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    var shakeDetected by remember { mutableStateOf(false) }

    // Listener para detectar o balanço do celular
    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val gForce = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH
                    if (gForce > 2.7) { // Valor de sensibilidade para detectar um balanço
                        shakeDetected = true
                    }
                }
            }
        }
    }

    // Registrar o listener para o acelerômetro
    DisposableEffect(Unit) {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    // Estado para item aleatório e controle do modal
    var randomItem by remember { mutableStateOf<TrendingItem?>(null) }
    var showModal by remember { mutableStateOf(false) }

    if (shakeDetected) {
        LaunchedEffect(shakeDetected) {
            loadTrendingMoviesAndSeries(1) { trendingItems ->
                if (trendingItems.isNotEmpty()) {
                    randomItem = trendingItems[Random.nextInt(trendingItems.size)]
                    showModal = true // Abrir o modal quando um item é sugerido
                }
            }
            shakeDetected = false // Resetar a detecção após a sugestão
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color(0xFF90CAF9), RoundedCornerShape(16.dp)) // Cor de fundo arredondada
                .padding(24.dp) // Padding interno
                .align(Alignment.CenterHorizontally) // Centralizar
        ) {
            Row {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Texto em coluna
                    Text(
                        text = "Não sabe o que assistir?",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Balance o celular!",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        ),
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.movement_phone), // Substitua pelo ID da sua imagem
                    contentDescription = "Imagem decorativa",
                    modifier = Modifier
                        .size(100.dp) // Tamanho da imagem
                        .clip(RoundedCornerShape(8.dp)) // Bordas arredondadas para a imagem
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Modal para exibir detalhes do item sugerido
        if (randomItem == null) {

        }
        if (showModal && randomItem != null) {
            AlertDialog(
                onDismissRequest = { showModal = false },
                title = {
                    (randomItem?.title ?: randomItem?.name)?.let { Text(text = it) }
                },
                text = {
                    Column {
                        randomItem?.poster_path?.let { imageUrl ->
                            val posterUrl = AppConstants.TMDB_IMAGE_BASE_URL_ORIGINAL + imageUrl
                            // Exibir imagem do item
                            AsyncImage(
                                model = posterUrl,
                                contentDescription = "Imagem de ${randomItem?.title}",
                                modifier = Modifier
                                    .fillMaxWidth() // Preencher a largura do modal
                                    .height(180.dp) // Altura fixa para a imagem
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Transparent), // Cor de fundo enquanto carrega a imagem
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp)) // Menos espaço entre imagem e descrição

                        // Exibir mais informações
                        Text(
                            text = randomItem?.overview ?: "Sem descrição",
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (randomItem?.isMovie() == true) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Data de Lançamento: ")
                                    }
                                    append(formatDateToBrazilian(randomItem?.release_date))
                                },
                                style = TextStyle(fontSize = 16.sp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Média de Voto: ")
                                }
                                append("${randomItem?.vote_average ?: "N/A"} (${randomItem?.vote_count ?: 0} votos)")
                            },
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Linguagem Original: ")
                                }
                                append(randomItem?.original_language ?: "N/A")
                            },
                            style = TextStyle(fontSize = 16.sp)
                        )

                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showModal = false }
                    ) {
                        Text("Fechar")
                    }
                }
            )
        }


        Text(
            text = "Tendências do Momento",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )

        TrendingMoviesAndSeriesCarousel()

        Spacer(modifier = Modifier.height(16.dp))

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
        FilmesPopularesCarousel()

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
        SeriesPopularesCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Filmes bem Avaliados",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 5.dp)
                .align(Alignment.Start)
        )
        FilmesBemAvaliadosCarousel(navController)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Séries bem Avaliadas",
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
            text = "Filmes em Cartaz",
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
            text = "Series em Exibição",
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
            text = "Filmes por Vir",
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

        Spacer(modifier = Modifier.weight(1f))
    }
}
