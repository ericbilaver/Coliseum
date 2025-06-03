import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moviebase.tmdb.model.TmdbMovieDetail
import com.coliseum.app.TmdbClient
import com.coliseum.app.model.Movie
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MovieViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState
    val tmdbMovie = MutableStateFlow<TmdbMovieDetail?>(null)

    suspend fun getTMDBMovieDetails(movieId: Int) {
        // TMDB setup
        tmdbMovie.value = TmdbClient.tmdb.movies.getDetails(movieId)
    }

    fun loadMovie(movieId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Get QuerySnapshot instead of DocumentSnapshot
                val querySnapshot = db.collection("movies-test")
                    .whereEqualTo("tmdbid", movieId.toLong()) // Convert to Long if movieId is String
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    // Get the first document from the query results
                    val document = querySnapshot.documents.first()
                    val movie = document.toObject(Movie::class.java)?.copy(id = document.id)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        movie = movie,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Movie not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error loading movie: ${e.message}"
                )
            }
        }
    }

    fun updateMovie(movieId: String, movie: Movie) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val movieMap = hashMapOf<String, Any>(
                    "title" to movie.title,
                    "tmdbid" to movie.tmdbid,
                    "aspectRatios" to movie.aspectRatios,
                    "extraformat" to movie.extraformat,
                    "imax" to movie.imax,
                    "information" to movie.information,
                    "notes" to movie.notes
                )

                // Get QuerySnapshot instead of DocumentSnapshot
                val querySnapshot = db.collection("movies-test")
                    .whereEqualTo("tmdbid", movieId.toLong()) // Convert to Long if movieId is String
                    .get()
                    .await()
                val yo = querySnapshot.documents.first()
                yo.reference.update(movieMap)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movie = movie,
                    success = "Movie updated successfully",
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error updating movie: ${e.message}"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, success = null)
    }
}

data class MovieUiState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: String? = null,
    val success: String? = null
)