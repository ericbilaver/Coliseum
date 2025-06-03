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
@Composable
fun MovieScreen(
    movieId: String,
    viewModel: MovieViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val tmdbMovie by viewModel.tmdbMovie.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        viewModel.loadOrCreateMovie(movieId)
        viewModel.getTMDBMovieDetails(movieId.toInt())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadOrCreateMovie(movieId) }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            uiState.movie != null -> {
                tmdbMovie?.let {
                    AsyncImage(
                        model = TmdbImageUrlBuilder.build(it.posterImage?.path ?: "", "w500"),
                        contentDescription = null
                    )
                }
                if (uiState.isCreatingNew) {
                    // Show create new movie card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Movie Not Found",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "No movie found with TMDB ID: ${uiState.movie!!.tmdbid}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showEditDialog = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Create New Movie")
                            }
                        }
                    }
                } else {
                    // Show existing movie
                    MovieInfoCard(
                        movie = uiState.movie!!,
                        onEditClick = { showEditDialog = true }
                    )
                }
            }
        }
    }

    // Show dialog for both create and edit
    if (showEditDialog && uiState.movie != null) {
        EditMovieDialog(
            movie = uiState.movie!!,
            tmdbMovie = tmdbMovie,
            movieId = if (uiState.isCreatingNew) null else uiState.movie!!.id,
            isCreating = uiState.isCreatingNew,
            onDismiss = {
                showEditDialog = false
                viewModel.clearMessages()
            },
            onSave = { id, updatedMovie ->
                if (uiState.isCreatingNew) {
                    viewModel.createMovie(updatedMovie)
                } else {
                    viewModel.updateMovie(id!!, updatedMovie)
                }
                showEditDialog = false
            }
        )
    }

    // Show success message
    uiState.success?.let { message ->
        LaunchedEffect(message) {
            // Show snackbar or toast
            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
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