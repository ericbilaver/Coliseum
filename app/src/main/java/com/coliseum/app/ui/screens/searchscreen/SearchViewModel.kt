package com.coliseum.app.ui.screens.searchscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moviebase.tmdb.model.TmdbMovie
import com.coliseum.app.TmdbClient
import com.coliseum.app.model.TheatreSearchSuggestion
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {
    val searchQuery = MutableStateFlow("")
    val movieList = mutableStateListOf<TmdbMovie>()
    val theatreList = mutableStateListOf<TheatreSearchSuggestion>()
    val searchType = MutableStateFlow(SearchType.THEATRES)

    fun onQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onSearchTypeChange(newSearchType: SearchType) {
        searchType.value = newSearchType
        movieList.removeAll(movieList)
        theatreList.removeAll(theatreList)
    }

    fun updateSuggestions(searchQuery: String) {
        if (searchType.value == SearchType.THEATRES) {
            updateTheatreSuggestions(searchQuery)
        }
        else {
            updateMovieSuggestions(searchQuery)
        }
    }

    fun updateTheatreSuggestions(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Firebase.firestore
                .collection("theatres-test")
                .whereGreaterThanOrEqualTo("name", searchQuery)
                .whereLessThanOrEqualTo("name", "$searchQuery\uF7FF")
                .get()
                .addOnSuccessListener { documents ->
                    theatreList.removeAll(theatreList)
                    for (document in documents) {
                        //println("${document.id} => ${document.data}")
                        println(document.data["name"])
                        theatreList.add (TheatreSearchSuggestion(document.data["name"].toString(), document.id))
                    }
                }
                .addOnFailureListener { exception ->
                    println(exception)
                }
        }
    }

    fun updateMovieSuggestions(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            movieList.removeAll(movieList)
            movieList.addAll(TmdbClient.tmdb.search.findMovies(
                query = searchQuery,
                page = 1
            ).results)
        }
    }


}