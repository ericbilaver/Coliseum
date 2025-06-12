package com.coliseum.app.ui.screens.searchscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coliseum.app.ui.screens.homescreen.HomeViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.getValue



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onTheatreClick: (theatreId: String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    var theatreList = viewModel.theatreList
    var movieList = viewModel.movieList

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
    Button(
        onClick = { viewModel.updateSearchSuggestions(searchQuery) }
    ) {
        Text("Search")
    }
    Text("Movies")
    movieList.forEachIndexed { index, movie ->
        Text(movie.title)
    }
    Text("Theatres")
    theatreList.forEachIndexed { index, theatre ->
        Text(theatre.name, modifier = Modifier.clickable {onTheatreClick(theatre.id)})
    }

}