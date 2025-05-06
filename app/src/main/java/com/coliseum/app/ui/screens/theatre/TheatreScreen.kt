package com.coliseum.app.ui.screens.theatre

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import coil3.compose.AsyncImage

@Composable
fun TheatreScreen(
    viewModel: TheatreViewModel = viewModel(),
    theatreId: String? = ""
) {
    val theatreInfo by viewModel.theatreInfo.collectAsState()

    theatreInfo?.let {
        if (it.images.isNotEmpty()) {
            AsyncImage(
                model = it.images[0],
                contentDescription = null
            )
        }
        Text(it.name)
        Text("${it.address}, ${it.city}")
        Text("Total Auditoriums: ${it.totalAuditoriums}")

    }



    LaunchedEffect(Unit) {
        viewModel.getTheatreInfo(theatreId ?: "")
    }
}