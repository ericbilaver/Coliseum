package com.coliseum.app.ui.screens.theatre

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.coliseum.app.model.Theatre
import com.coliseum.app.ui.theme.PurpleGrey40

@Composable
fun TheatreScreen(
    theatreId: String,
    viewModel: TheatreViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(theatreId) {
        viewModel.fetchTheatre(theatreId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .height(1500.dp)
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
                            onClick = { viewModel.fetchTheatre(theatreId) }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            uiState.theatre != null -> {
                if (uiState.isCreatingNew) {
                    // Show create new theatre card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Theatre Not Found",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "No theatre found with name: ${uiState.theatre!!.name}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showEditDialog = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Create New Theatre")
                            }
                        }
                    }
                } else {
                    if (uiState.theatre?.images?.isNotEmpty() == true) {
                        AsyncImage(uiState.theatre?.images?.get(0), contentDescription = null)
                    }
                    // Show existing theatre
                    TheatreInfoCard(
                        theatre = uiState.theatre!!,
                        onEditClick = { showEditDialog = true }
                    )
                }
            }
        }
    }

    // Show dialog for both create and edit
    if (showEditDialog && uiState.theatre != null) {
        EditTheatreDialog(
            theatre = uiState.theatre!!,
            theatreId = if (uiState.isCreatingNew) null else uiState.theatre!!.id,
            isCreating = uiState.isCreatingNew,
            onDismiss = {
                showEditDialog = false
                viewModel.clearMessages()
            },
            onSave = { id, updatedTheatre ->
                if (uiState.isCreatingNew) {
                    viewModel.createTheatre(updatedTheatre)
                } else {
                    viewModel.updateTheatre(id!!, updatedTheatre)
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
fun AuditoriumsListings(
    theatreInfo: Theatre,
    onAuditoriumClick: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.SpaceBetween,
        userScrollEnabled = false
    ) {
        items(theatreInfo.totalAuditoriums) { auditoriumIndex ->
            val auditorium = theatreInfo.auditoriums.find { it.number == (auditoriumIndex + 1) }
            // if found special format
            if (auditorium != null) {
                val formatList = auditorium.type.split("/")
                Column(
                    modifier = Modifier.height(80.dp).background(PurpleGrey40)
                ) {
                    Text(auditorium.number.toString(), color = Color.White)
                    formatList.forEach { Text(it, color = Color.White, fontSize = 15.sp) }
                }
            }
            // regular theatre
            else {
                Column(
                    modifier = Modifier.height(80.dp).background(Color.Black)
                ) {
                    Text((auditoriumIndex + 1).toString(), color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun TheatreInfoCard(
    theatre: Theatre,
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
                    text = theatre.name,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Button(onClick = onEditClick) {
                Text("Edit")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (theatre.address.isNotEmpty()) {
                InfoRow("Address", theatre.address)
            }

            if (theatre.city.isNotEmpty() || theatre.province.isNotEmpty()) {
                val location = buildString {
                    if (theatre.city.isNotEmpty()) append(theatre.city)
                    if (theatre.province.isNotEmpty()) {
                        if (length > 0) append(", ")
                        append(theatre.province)
                    }
                    if (theatre.postalCode.isNotEmpty()) {
                        if (length > 0) append(" ")
                        append(theatre.postalCode)
                    }
                }
                if (location.isNotEmpty()) {
                    InfoRow("Location", location)
                }
            }

            if (theatre.phoneNumber.isNotEmpty()) {
                InfoRow("Phone", theatre.phoneNumber)
            }

            if (theatre.capacity > 0) {
                InfoRow("Capacity", theatre.capacity.toString())
            }

            if (theatre.note.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow("Note", theatre.note)
            }
        }
    }
    if (theatre.totalAuditoriums > 0) {
        AuditoriumsListings(theatreInfo = theatre, onAuditoriumClick = {})
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}