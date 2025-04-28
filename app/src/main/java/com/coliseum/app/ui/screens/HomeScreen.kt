package com.coliseum.app.ui.screens

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
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
import com.coliseum.app.TmdbClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClick: (movieId: Int) -> Unit
) {
    var movieList by remember { mutableStateOf<List<TmdbMovie>>(listOf()) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    SearchBar(
        query = searchQuery,
        onQueryChange = { searchQuery = it },
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
        if (searchQuery.length > 4)
            movieList = TmdbClient.tmdb.search.findMovies(
                query = searchQuery,
                page = 1
            ).results
    }
}


