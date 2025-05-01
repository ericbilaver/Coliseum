package com.coliseum.app.ui.screens.movie

import androidx.lifecycle.ViewModel
import app.moviebase.tmdb.model.TmdbMovieDetail
import com.coliseum.app.TmdbClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(): ViewModel() {
    val movieInfo = MutableStateFlow<TmdbMovieDetail?>(null)

    suspend fun getMovieDetails(movieId: Int) {
        // TMDB setup
        movieInfo.value = TmdbClient.tmdb.movies.getDetails(movieId)
    }
}