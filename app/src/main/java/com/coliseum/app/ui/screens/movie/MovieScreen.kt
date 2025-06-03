package com.coliseum.app.ui.screens.movie

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.coliseum.app.R


@Composable
fun MovieScreen(
    viewModel: MovieViewModel = viewModel(),
    movieId: Int?
) {
    val movieInfo by viewModel.movieInfo.collectAsState()
    val formatInfo by viewModel.formatInfo.collectAsState()

    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500/${movieInfo?.posterImage?.path}",
            contentDescription = null
        )
        movieInfo?.let {
            Text(it.title, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(text = it.tagline, fontStyle = FontStyle.Italic, textAlign = TextAlign.Center)
            Text("${it.runtime}mins")
            formatInfo?.let {
                FormatImageResolver(it.format ?: MovieFormat.PartialFilm)
                Text(it.aspectRatios.toString())
            }
            Text(it.overview)


        }
    }

    LaunchedEffect(Unit) {
        // TMDB setup
        viewModel.getMovieDetails(movieId ?: 0)
        viewModel.checkFirebaseMovie(movieId ?: 0)
    }
}

@Composable
fun FormatImageResolver(format: MovieFormat) {
    val imageID = when(format) {
        MovieFormat.PartialFilm -> R.drawable.imax_70mm_logo
        MovieFormat.EntirelyFilm -> R.drawable.imax_70mm_logo
        MovieFormat.PartialDigital -> R.drawable.imax_logo_blue
        MovieFormat.EntirelyDigital -> R.drawable.imax_logo_blue
    }

    Image(
        painter = painterResource(imageID),
        contentDescription = null,
        modifier = Modifier.width(100.dp)
    )
}