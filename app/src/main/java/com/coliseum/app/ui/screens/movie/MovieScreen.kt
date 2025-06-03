import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.moviebase.tmdb.image.TmdbImageUrlBuilder
import coil3.compose.AsyncImage
import com.coliseum.app.model.Movie
import com.coliseum.app.ui.screens.movie.EditMovieDialog

@Composable
fun MovieScreen(
    movieId: String,
    viewModel: MovieViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val tmdbMovie by viewModel.tmdbMovie.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
        viewModel.getTMDBMovieDetails(movieId.toInt())
    }

    // Handle success/error messages
    LaunchedEffect(uiState.success, uiState.error) {
        // You can show snackbars or other notifications here
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display movie information
        val posterUrl = TmdbImageUrlBuilder.build(tmdbMovie?.posterImage?.path ?: "", "w500")
        println(posterUrl)
        AsyncImage(
            model = posterUrl,
            contentDescription = null
        )
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.error ?: "",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            uiState.movie != null -> {

                Spacer(Modifier.height(16.dp))
                MovieInfoCard(
                    movie = uiState.movie!!,
                    onEditClick = { showEditDialog = true }
                )
            }
        }
    }

    // Show edit dialog
    if (showEditDialog && uiState.movie != null) {
        EditMovieDialog(
            movie = uiState.movie!!,
            movieId = movieId,
            onDismiss = {
                showEditDialog = false
                viewModel.clearMessages()
            },
            onSave = { id, updatedMovie ->
                viewModel.updateMovie(id, updatedMovie)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun MovieInfoCard(
    movie: Movie,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Button(onClick = onEditClick) {
                    Text("Edit")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("TMDB ID: ${movie.tmdbid}")
            Text("Aspect Ratios: ${movie.aspectRatios.joinToString(", ")}")
            Text("Extra Format: ${movie.extraformat}")
            Text("IMAX: ${movie.imax}")
            Text("Information: ${movie.information}")
            Text("Notes: ${movie.notes}")
        }
    }
}