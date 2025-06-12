package com.coliseum.app.ui.screens.homescreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coliseum.app.ui.common.MovieCarousel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onMovieClick: (movieId: Int) -> Unit
) {
    val currentMoviesList by viewModel.nowPlayingMovies.collectAsState()
    val comingSoonMoviesList by viewModel.comingSoonMovies.collectAsState()

    Text("Now Playing")
    MovieCarousel(
        currentMoviesList,
        onMovieClick
    )

    Text("Coming Soon")
    MovieCarousel(
        comingSoonMoviesList,
        onMovieClick
    )

    LaunchedEffect(Unit) {
        viewModel.getNowPlayingMovies()
        viewModel.getComingSoonMovies()
    }
}


