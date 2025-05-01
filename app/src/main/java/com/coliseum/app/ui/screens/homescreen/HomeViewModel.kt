package com.coliseum.app.ui.screens.homescreen


import androidx.lifecycle.ViewModel
import app.moviebase.tmdb.model.TmdbMovie
import app.moviebase.tmdb.model.TmdbMovieDetail
import com.coliseum.app.TmdbClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    val searchQuery = MutableStateFlow("")
    val movieList = MutableStateFlow<List<TmdbMovie>>(listOf())

    fun onQueryChange(query: String) {
        searchQuery.value = query
    }

    suspend fun updateSearchSuggestions(searchQuery: String) {
        movieList.value = TmdbClient.tmdb.search.findMovies(
            query = searchQuery,
            page = 1
        ).results
    }
}