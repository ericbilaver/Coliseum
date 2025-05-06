package com.coliseum.app.ui.screens.homescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.moviebase.tmdb.model.TmdbMovie
import coil3.compose.AsyncImage
import com.coliseum.app.TmdbClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onClick: (movieId: Int) -> Unit
) {
    val movieList by viewModel.movieList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var isSearchActive by remember { mutableStateOf(false) }

    SearchBar(
        query = searchQuery,
        onQueryChange = { viewModel.onQueryChange(it) },
        onSearch = {

        },
        active = isSearchActive,
        onActiveChange = {  }
    ) {

    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4)
    ) {
        items(movieList.size) { index ->
            val movie = movieList[index]
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500/${movie.posterImage?.path}",
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        onClick(movie.id)
                    }
            )
        }

    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 3)
            viewModel.updateSearchSuggestions(searchQuery)
    }
}


