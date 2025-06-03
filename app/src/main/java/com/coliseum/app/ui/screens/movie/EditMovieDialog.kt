import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.moviebase.tmdb.model.TmdbMovie
import app.moviebase.tmdb.model.TmdbMovieDetail
import com.coliseum.app.model.Movie

@Composable
fun EditMovieDialog(
    movie: Movie,
    tmdbMovie: TmdbMovieDetail?,
    movieId: String? = null, // null when creating new movie
    isCreating: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (String?, Movie) -> Unit // movieId is null for new movies
) {
    var title by remember { mutableStateOf(movie.title) }
    var tmdbid by remember { mutableStateOf(movie.tmdbid.toString()) }
    var aspectRatios by remember { mutableStateOf(movie.aspectRatios.joinToString(", ")) }
    var extraformat by remember { mutableStateOf(movie.extraformat) }
    var imax by remember { mutableStateOf(movie.imax) }
    var information by remember { mutableStateOf(movie.information) }
    var notes by remember { mutableStateOf(movie.notes) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(max = 600.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCreating) "Create New Movie" else "Edit Movie",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Show TMDB ID info for new movies
                if (isCreating) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = "Creating new movie with TMDB ID: ${movie.tmdbid}",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title Field (required for new movies)
                OutlinedTextField(
                    value = title,
                    onValueChange = {

                    },
                    label = {
                        Text(if (isCreating) "Title *" else "Title")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                // TMDB ID Field (read-only for new movies)
                OutlinedTextField(
                    value = tmdbid,
                    onValueChange = {},
                    label = { Text("TMDB ID") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = false
                )

                // ... rest of the fields remain the same ...
                Spacer(modifier = Modifier.height(12.dp))

                // Aspect Ratios Field
                OutlinedTextField(
                    value = aspectRatios,
                    onValueChange = { aspectRatios = it },
                    label = { Text("Aspect Ratios") },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = { Text("Comma separated values") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Extra Format Field
                OutlinedTextField(
                    value = extraformat,
                    onValueChange = { extraformat = it },
                    label = { Text("Extra Format") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // IMAX Field
                OutlinedTextField(
                    value = imax,
                    onValueChange = { imax = it },
                    label = { Text("IMAX") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Information Field
                OutlinedTextField(
                    value = information,
                    onValueChange = { information = it },
                    label = { Text("Information") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Notes Field
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {

                            val tmdbidLong = tmdbid.trim().toLongOrNull()

                            val aspectRatiosList = if (aspectRatios.trim().isNotEmpty()) {
                                aspectRatios.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            } else {
                                listOf()
                            }

                            val updatedMovie = Movie(
                                id = movieId ?: "",
                                title = title.trim(),
                                tmdbid = tmdbidLong ?: movie.tmdbid,
                                aspectRatios = aspectRatiosList,
                                extraformat = extraformat.trim(),
                                imax = imax.trim(),
                                information = information.trim(),
                                notes = notes.trim()
                            )

                            onSave(movieId, updatedMovie)

                        }
                    ) {
                        Text(if (isCreating) "Create" else "Save")
                    }
                }
            }
        }
    }
}