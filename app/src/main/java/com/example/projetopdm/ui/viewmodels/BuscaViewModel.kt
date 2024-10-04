import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetopdm.AppConstants
import com.example.projetopdm.model.MediaItem
import com.example.projetopdm.network.RetrofitInstance
import com.example.projetopdm.network.TmdbMovieResponse
import com.example.projetopdm.network.TmdbSerieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscaViewModel : ViewModel() {

    var searchResults = mutableStateOf<List<MediaItem>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    var isLoadingMore = mutableStateOf(false)
        private set

    fun buscarFilmesESeries(query: String, page: Int) {
        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val filmesCall = RetrofitInstance.api.searchMovies(AppConstants.TMDB_API_KEY, query, "pt-BR", page)
            val seriesCall = RetrofitInstance.api.searchTVShows(AppConstants.TMDB_API_KEY, query, "pt-BR", page)

            val mediaItems = mutableListOf<MediaItem>()

            filmesCall.enqueue(object : Callback<TmdbMovieResponse> {
                override fun onResponse(call: Call<TmdbMovieResponse>, response: Response<TmdbMovieResponse>) {
                    response.body()?.let {
                        mediaItems.addAll(it.results)
                    }
                    seriesCall.enqueue(object : Callback<TmdbSerieResponse> {
                        override fun onResponse(call: Call<TmdbSerieResponse>, response: Response<TmdbSerieResponse>) {
                            response.body()?.let {
                                mediaItems.addAll(it.results)
                            }
                            searchResults.value = mediaItems
                            isLoading.value = false
                        }

                        override fun onFailure(call: Call<TmdbSerieResponse>, t: Throwable) {
                            searchResults.value = mediaItems
                            isLoading.value = false
                        }
                    })
                }

                override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                    searchResults.value = emptyList()
                    isLoading.value = false
                }
            })
        }
    }
}
