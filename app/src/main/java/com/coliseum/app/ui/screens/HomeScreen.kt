package com.coliseum.app.ui.screens

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.moviebase.tmdb.Tmdb3
import app.moviebase.tmdb.model.TmdbMovie
import coil3.compose.AsyncImage
import com.coliseum.app.BuildConfig

@Composable
fun HomeScreen() {
    var movieList by remember { mutableStateOf<List<TmdbMovie>>(listOf()) }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        movieList.forEach {
            println(it.posterImage)
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500/${it.posterImage?.path}",
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }

    LaunchedEffect(Unit) {
        // TMDB setup
        val tmdb = Tmdb3(
            tmdbApiKey = BuildConfig.TMDB_API_KEY
        )
        movieList = tmdb.search.findMovies(
            query = "sinners",
            page = 1
        ).results
    }
}