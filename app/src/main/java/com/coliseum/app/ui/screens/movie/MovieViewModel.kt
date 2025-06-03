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

    fun createMovie(movie: Movie) {
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

                // Add new document to Firestore
                val documentRef = db.collection("movies-test").add(movieMap).await()

                val createdMovie = movie.copy(id = documentRef.id)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movie = createdMovie,
                    success = "Movie created successfully",
                    error = null,
                    isCreatingNew = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error creating movie: ${e.message}"
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

                db.collection("movies-test").document(movieId).update(movieMap).await()

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

    fun loadOrCreateMovie(movieId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val tmdbId = movieId.toLongOrNull()
                if (tmdbId == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Invalid movie ID format"
                    )
                    return@launch
                }

                val querySnapshot = db.collection("movies-test")
                    .whereEqualTo("tmdbid", tmdbId)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    // Movie exists, load it
                    val document = querySnapshot.documents.first()
                    val movie = document.toObject(Movie::class.java)?.copy(id = document.id)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        movie = movie,
                        error = null,
                        isCreatingNew = false
                    )
                } else {
                    // Movie doesn't exist, prepare for creation
                    val newMovie = Movie(
                        tmdbid = tmdbId,
                        title = tmdbMovie.value?.title ?: "", // Will be filled by user
                        aspectRatios = listOf(),
                        extraformat = "",
                        imax = "",
                        information = "",
                        notes = ""
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        movie = newMovie,
                        error = null,
                        isCreatingNew = true
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

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, success = null)
    }
}

data class MovieUiState(
    val isLoading: Boolean = false,
    val isCreatingNew: Boolean = false, // Add this
    val movie: Movie? = null,
    val error: String? = null,
    val success: String? = null
)