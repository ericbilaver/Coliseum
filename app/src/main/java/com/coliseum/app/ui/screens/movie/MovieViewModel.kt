package com.coliseum.app.ui.screens.movie

import androidx.lifecycle.ViewModel
import app.moviebase.tmdb.model.TmdbMovieDetail
import com.coliseum.app.TmdbClient
import com.coliseum.app.ui.screens.movie.MovieFormat.EntirelyDigital
import com.coliseum.app.ui.screens.movie.MovieFormat.EntirelyFilm
import com.coliseum.app.ui.screens.movie.MovieFormat.PartialDigital
import com.coliseum.app.ui.screens.movie.MovieFormat.PartialFilm
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

enum class MovieFormat {
    EntirelyFilm, PartialFilm, EntirelyDigital, PartialDigital;
    companion object {
        fun stringToFormat(string: String): MovieFormat? {
            return when (string) {
                "digitalEntire" -> EntirelyDigital
                "digitalPartial" -> PartialDigital
                "65mmEntire" -> EntirelyFilm
                "65mmPartial" -> PartialFilm
                else -> null
            }
        }
    }
}

@HiltViewModel
class MovieViewModel @Inject constructor(): ViewModel() {
    val movieInfo = MutableStateFlow<TmdbMovieDetail?>(null)
    val formatInfo = MutableStateFlow<MovieFormat?>(null)

    suspend fun getMovieDetails(movieId: Int) {
        // TMDB setup
        movieInfo.value = TmdbClient.tmdb.movies.getDetails(movieId)
    }

    suspend fun checkMovieFormat(movieId: Int) {
        val result = Firebase.firestore.collection("movies-test")
            .whereEqualTo("tmdbid", movieId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                println(documentSnapshot)
            }
            .addOnFailureListener { exception ->
                println(exception)
            }

        result
            .addOnSuccessListener { documentSnapshot ->
                println(documentSnapshot)
                if (documentSnapshot.documents.isNotEmpty()) {
                    val docToGet = documentSnapshot.documents[0]
                    formatInfo.value =
                        MovieFormat.stringToFormat(docToGet.get ("imax").toString())
                }
            }
            .addOnFailureListener { exception ->
                println("Error checking document: ${exception.message}")
            }
    }
}