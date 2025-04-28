package com.coliseum.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import app.moviebase.tmdb.Tmdb3
import app.moviebase.tmdb.model.TmdbMovieDetail
import coil3.compose.AsyncImage
import com.coliseum.app.BuildConfig
import java.util.Locale

@Composable
fun MovieScreen(movieId: Int?) {
    var movieInfo by remember { mutableStateOf<TmdbMovieDetail?>(null) }
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500/${movieInfo?.posterImage?.path}",
            contentDescription = null
        )
        movieInfo?.let {
            Text(it.title)
            Text("${it.runtime}mins")
            Text(it.overview)

        }
    }

    LaunchedEffect(Unit) {
        // TMDB setup
        val tmdb = Tmdb3(
            tmdbApiKey = BuildConfig.TMDB_API_KEY
        )
        movieInfo = tmdb.movies.getDetails(movieId!!)
    }
}