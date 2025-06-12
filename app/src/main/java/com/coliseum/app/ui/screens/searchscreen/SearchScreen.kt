package com.coliseum.app.ui.screens.searchscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onTheatreClick: (theatreId: String) -> Unit,
    onMovieClick: (movieId: Int) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    var theatreList = viewModel.theatreList
    var movieList = viewModel.movieList
    val searchType = viewModel.searchType.collectAsState()

    Text("Search")
    SearchBar(
        query = searchQuery,
        onQueryChange = { viewModel.onQueryChange(it) },
        onSearch = {


        },
        active = false,
        onActiveChange = {  }
    ) {

    }
    SearchToggle(
        selected = searchType.value,
        onSelectedChange = {
            viewModel.onSearchTypeChange(it)
        }
    )
    Button(
        onClick = { viewModel.updateSuggestions(searchQuery) }
    ) {
        Text("Search")
    }
    if (movieList.isNotEmpty()) {
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
                            onMovieClick(movie.id)
                        }
                )
            }

        }
    }

    theatreList.forEachIndexed { index, theatre ->
        Text(theatre.name, modifier = Modifier.clickable {onTheatreClick(theatre.id)})
    }

}