package com.coliseum.app.ui.screens.movie

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.coliseum.app.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMovieDialog(
    movie: Movie,
    movieId: String,
    onDismiss: () -> Unit,
    onSave: (String, Movie) -> Unit
) {
    var title by remember { mutableStateOf(movie.title) }
    var tmdbid by remember { mutableStateOf(movie.tmdbid.toString()) }
    var aspectRatios by remember { mutableStateOf(movie.aspectRatios.joinToString(", ")) }
    var extraformat by remember { mutableStateOf(movie.extraformat) }
    var imax by remember { mutableStateOf(movie.imax) }
    var information by remember { mutableStateOf(movie.information) }
    var notes by remember { mutableStateOf(movie.notes) }

    var titleError by remember { mutableStateOf(false) }
    var tmdbidError by remember { mutableStateOf(false) }

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
                        text = "Edit Movie",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title Field
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = false
                    },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = titleError,
                    supportingText = if (titleError) {
                        { Text("Title is required") }
                    } else null
                )

                Spacer(modifier = Modifier.height(12.dp))

                // TMDB ID Field
                OutlinedTextField(
                    value = tmdbid,
                    onValueChange = {
                        tmdbid = it
                        tmdbidError = false
                    },
                    label = { Text("TMDB ID") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = tmdbidError,
                    supportingText = if (tmdbidError) {
                        { Text("Invalid TMDB ID") }
                    } else null
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Aspect Ratios Field
                OutlinedTextField(
                    value = aspectRatios,
                    onValueChange = { aspectRatios = it },
                    label = { Text("Aspect Ratios") },
                    placeholder = { Text("1.43:1, 2.76:1") },
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
                            // Validate inputs
                            titleError = title.trim().isEmpty()

                            val tmdbidLong = tmdbid.trim().toLongOrNull()
                            tmdbidError = tmdbid.trim().isNotEmpty() && tmdbidLong == null

                            if (!titleError && !tmdbidError) {
                                val aspectRatiosList = if (aspectRatios.trim().isNotEmpty()) {
                                    aspectRatios.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                } else {
                                    listOf()
                                }

                                val updatedMovie = Movie(
                                    id = movieId,
                                    title = title.trim(),
                                    tmdbid = tmdbidLong ?: 0L,
                                    aspectRatios = aspectRatiosList,
                                    extraformat = extraformat.trim(),
                                    imax = imax.trim(),
                                    information = information.trim(),
                                    notes = notes.trim()
                                )

                                onSave(movieId, updatedMovie)
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}