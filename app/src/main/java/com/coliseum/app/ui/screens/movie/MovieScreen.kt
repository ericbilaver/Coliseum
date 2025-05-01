package com.coliseum.app.ui.screens.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import app.moviebase.tmdb.model.TmdbMovieDetail
import coil3.compose.AsyncImage
import com.coliseum.app.TmdbClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO add IMAX section
// TODO add viewmodel

@Composable
fun MovieScreen(
    viewModel: MovieViewModel = viewModel(),
    movieId: Int?
) {
    val movieInfo by viewModel.movieInfo.collectAsState()
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500/${movieInfo?.posterImage?.path}",
            contentDescription = null
        )
        movieInfo?.let {
            Text(it.title, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(text = it.tagline, fontStyle = FontStyle.Italic, textAlign = TextAlign.Center)
            Text("${it.runtime}mins")
            Text(it.overview)
        }
    }

    LaunchedEffect(Unit) {
        // TMDB setup
        viewModel.getMovieDetails(movieId ?: 0)
    }
}