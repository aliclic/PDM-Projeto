package com.example.projetopdm.ui.telas

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
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
fun MovieItem(movie: Movie, onClick: () -> Unit) {
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
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
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

// Função composable para exibir o modal com os detalhes do filme
@Composable
fun MovieDetailsModal(movie: Movie?, onDismiss: () -> Unit) {
    if (movie != null) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                ) {
                    // Exibe o título do filme
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Exibe a descrição
                    Text(text = movie.overview)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Botão para fechar o modal
                        Button(onClick = { onDismiss() }) {
                            Text(text = "Fechar")
                        }

                        // Ícone de lista à direita
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.List, // Ícone de lista padrão do Material
                                contentDescription = "Lista"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SerieItem(serie: Serie, onClick: () -> Unit) {
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
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
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

// Função composable para exibir o modal com os detalhes do filme
@Composable
fun SerieDetailsModal(serie: Serie?, onDismiss: () -> Unit) {
    if (serie != null) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                ) {
                    // Exibe o nome da serie
                    Text(
                        text = serie.name,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Exibe a descrição
                    Text(text = serie.overview)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Botão para fechar o modal
                        Button(onClick = { onDismiss() }) {
                            Text(text = "Fechar")
                        }

                        // Ícone de lista à direita
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.List, // Ícone de lista padrão do Material
                                contentDescription = "Lista"
                            )
                        }
                    }
                }
            }
        }
    }
}

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
                val seriesList = (it as? TmdbSeriesResponse)?.results ?: emptyList()
                onSeriesLoaded(seriesList)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e("API_ERROR", "Error fetching series", t)
            onSeriesLoaded(emptyList())  // Retorna uma lista vazia se falhar
        }
    })
}

// Carrega filmes populares
private fun loadPopularMovies(page: Int, onMoviesLoaded: (List<Movie>) -> Unit) {
    val call = RetrofitInstance.api.getPopularMovies(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadMovies(call, onMoviesLoaded)
}

// Carrega filmes mais bem avaliados
private fun loadTopRatedMovies(page: Int, onMoviesLoaded: (List<Movie>) -> Unit) {
    val call = RetrofitInstance.api.getTopRatedMovies(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadMovies(call, onMoviesLoaded)
}

// Carrega filmes em cartaz
private fun loadNowPlayingMovies(page: Int, onMoviesLoaded: (List<Movie>) -> Unit) {
    val call = RetrofitInstance.api.getNowPlayingMovies(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadMovies(call, onMoviesLoaded)
}

// Carrega filmes por vir
private fun getUpcomingMovies(page: Int, onMoviesLoaded: (List<Movie>) -> Unit) {
    val call = RetrofitInstance.api.getUpcomingMovies(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadMovies(call, onMoviesLoaded)
}

// Carrega séries populares
private fun loadPopularSeries(page: Int, onSeriesLoaded: (List<Serie>) -> Unit) {
    val call = RetrofitInstance.api.getPopularSeries(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadSeries(call, onSeriesLoaded)
}

// Carrega séries mais bem avaliadas
private fun loadTopRatedSeries(page: Int, onSeriesLoaded: (List<Serie>) -> Unit) {
    val call = RetrofitInstance.api.getTopRatedSeries(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadSeries(call, onSeriesLoaded)
}

// Carrega séries em exibição
private fun loadOnTheAirSeries(page: Int, onSeriesLoaded: (List<Serie>) -> Unit) {
    val call = RetrofitInstance.api.getOnTheAirSeries(AppConstants.TMDB_API_KEY, "pt-BR", page)
    loadSeries(call, onSeriesLoaded)
}

@Composable
fun FilmesPorVirCarousel() {
    var movies by remember { mutableStateOf(listOf<Movie>()) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var page by remember { mutableStateOf(1) }  // Variável para rastrear a página atual
    var isLoading by remember { mutableStateOf(false) }  // Variável para evitar múltiplas chamadas
    var isModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        getUpcomingMovies(page, onMoviesLoaded = {
            movies = it
            isLoading = false
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie, onClick = {
                selectedMovie = movie
                isModalVisible = true
            })
        }

        // Detecta quando chega ao final da lista e carrega mais
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LaunchedEffect(movies.size) {
                    isLoading = true
                    page++
                    getUpcomingMovies(page) { newMovies ->
                        movies = movies + newMovies
                        isLoading = false
                    }
                }
            }
        }
    }

    // Exibe o modal de detalhes se isModalVisible for true
    if (isModalVisible) {
        MovieDetailsModal(selectedMovie) {
            isModalVisible = false // Fecha o modal ao clicar no botão de fechar
        }
    }
}

@Composable
fun FilmesBemAvaliadosCarousel() {
    var movies by remember { mutableStateOf(listOf<Movie>()) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var page by remember { mutableStateOf(1) }  // Variável para rastrear a página atual
    var isLoading by remember { mutableStateOf(false) }  // Variável para evitar múltiplas chamadas
    var isModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        loadTopRatedMovies(page, onMoviesLoaded = {
            movies = it
            isLoading = false
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie, onClick = {
                selectedMovie = movie
                isModalVisible = true
            })
        }

        // Detecta quando chega ao final da lista e carrega mais
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LaunchedEffect(movies.size) {
                    isLoading = true
                    page++
                    loadTopRatedMovies(page) { newMovies ->
                        movies = movies + newMovies
                        isLoading = false
                    }
                }
            }
        }
    }

    // Exibe o modal de detalhes se isModalVisible for true
    if (isModalVisible) {
        MovieDetailsModal(selectedMovie) {
            isModalVisible = false // Fecha o modal ao clicar no botão de fechar
        }
    }
}

@Composable
fun SeriesBemAvaliadasCarousel() {
    var series by remember { mutableStateOf(listOf<Serie>()) }
    var selectedSerie by remember { mutableStateOf<Serie?>(null) }
    var page by remember { mutableStateOf(1) }  // Variável para rastrear a página atual
    var isLoading by remember { mutableStateOf(false) }  // Variável para evitar múltiplas chamadas
    var isModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        loadTopRatedSeries(page, onSeriesLoaded = {
            series = it
            isLoading = false
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(series) { serie ->
            SerieItem(serie, onClick = {
                selectedSerie = serie
                isModalVisible = true
            })
        }

        // Detecta quando chega ao final da lista e carrega mais
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LaunchedEffect(series.size) {
                    isLoading = true
                    page++
                    loadTopRatedSeries(page) { newSeries ->
                        series = series + newSeries
                        isLoading = false
                    }
                }
            }
        }
    }

    // Exibe o modal de detalhes se isModalVisible for true
    if (isModalVisible) {
        SerieDetailsModal(selectedSerie) {
            isModalVisible = false // Fecha o modal ao clicar no botão de fechar
        }
    }
}

@Composable
fun FilmesEmCartazCarousel() {
    var movies by remember { mutableStateOf(listOf<Movie>()) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var page by remember { mutableStateOf(1) }  // Variável para rastrear a página atual
    var isLoading by remember { mutableStateOf(false) }  // Variável para evitar múltiplas chamadas
    var isModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        loadNowPlayingMovies(page, onMoviesLoaded = {
            movies = it
            isLoading = false
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie, onClick = {
                selectedMovie = movie
                isModalVisible = true
            })
        }

        // Detecta quando chega ao final da lista e carrega mais
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LaunchedEffect(movies.size) {
                    isLoading = true
                    page++
                    loadNowPlayingMovies(page) { newMovies ->
                        movies = movies + newMovies
                        isLoading = false
                    }
                }
            }
        }
    }

    // Exibe o modal de detalhes se isModalVisible for true
    if (isModalVisible) {
        MovieDetailsModal(selectedMovie) {
            isModalVisible = false // Fecha o modal ao clicar no botão de fechar
        }
    }
}

@Composable
fun SeriesEmExibicaoCarousel() {
    var series by remember { mutableStateOf(listOf<Serie>()) }
    var selectedSerie by remember { mutableStateOf<Serie?>(null) }
    var page by remember { mutableStateOf(1) }  // Variável para rastrear a página atual
    var isLoading by remember { mutableStateOf(false) }  // Variável para evitar múltiplas chamadas
    var isModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        loadOnTheAirSeries(page, onSeriesLoaded = {
            series = it
            isLoading = false
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(series) { serie ->
            SerieItem(serie, onClick = {
                selectedSerie = serie
                isModalVisible = true
            })
        }

        // Detecta quando chega ao final da lista e carrega mais
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LaunchedEffect(series.size) {
                    isLoading = true
                    page++
                    loadOnTheAirSeries(page) { newSeries ->
                        series = series + newSeries
                        isLoading = false
                    }
                }
            }
        }
    }

    // Exibe o modal de detalhes se isModalVisible for true
    if (isModalVisible) {
        SerieDetailsModal(selectedSerie) {
            isModalVisible = false // Fecha o modal ao clicar no botão de fechar
        }
    }
}

// Filmes Populares
@Composable
fun FilmesPopularesCarousel() {
    var movies by remember { mutableStateOf(listOf<Movie>()) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var page by remember { mutableStateOf(1) }  // Variável para rastrear a página atual
    var isLoading by remember { mutableStateOf(false) }  // Variável para evitar múltiplas chamadas
    var isModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        loadPopularMovies(page, onMoviesLoaded = {
            movies = it
            isLoading = false
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie, onClick = {
                selectedMovie = movie
                isModalVisible = true
            })
        }

        // Detecta quando chega ao final da lista e carrega mais
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LaunchedEffect(movies.size) {
                    isLoading = true
                    page++
                    loadPopularMovies(page) { newMovies ->
                        movies = movies + newMovies
                        isLoading = false
                    }
                }
            }
        }
    }

    // Exibe o modal de detalhes se isModalVisible for true
    if (isModalVisible) {
        MovieDetailsModal(selectedMovie) {
            isModalVisible = false // Fecha o modal ao clicar no botão de fechar
        }
    }
}

// Series Populares
@Composable
fun SeriesPopularesCarousel() {
    var series by remember { mutableStateOf(listOf<Serie>()) }
    var selectedSerie by remember { mutableStateOf<Serie?>(null) }
    var page by remember { mutableStateOf(1) }  // Variável para rastrear a página atual
    var isLoading by remember { mutableStateOf(false) }  // Variável para evitar múltiplas chamadas
    var isModalVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        loadPopularSeries(page, onSeriesLoaded = {
            series = it
            isLoading = false
        })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(series) { serie ->
            SerieItem(serie, onClick = {
                selectedSerie = serie
                isModalVisible = true
            })
        }

        // Detecta quando chega ao final da lista e carrega mais
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LaunchedEffect(series.size) {
                    isLoading = true
                    page++
                    loadPopularSeries(page) { newSeries ->
                        series = series + newSeries
                        isLoading = false
                    }
                }
            }
        }
    }

    // Exibe o modal de detalhes se isModalVisible for true
    if (isModalVisible) {
        SerieDetailsModal(selectedSerie) {
            isModalVisible = false // Fecha o modal ao clicar no botão de fechar
        }
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

        Spacer(modifier = Modifier.weight(1f))
    }
}
