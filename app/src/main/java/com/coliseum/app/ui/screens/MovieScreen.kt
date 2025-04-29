package com.coliseum.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import app.moviebase.tmdb.Tmdb3
import app.moviebase.tmdb.model.TmdbMovieDetail
import coil3.compose.AsyncImage
import com.coliseum.app.BuildConfig
import com.coliseum.app.TmdbClient
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import java.util.Locale

@Composable
fun MovieScreen(movieId: Int?) {
    var movieInfo by remember { mutableStateOf<TmdbMovieDetail?>(null) }
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500/${movieInfo?.posterImage?.path}",
            contentDescription = null
        )
        movieInfo?.let {
            Text(it.title, fontWeight = FontWeight.Bold)
            Text(text = it.tagline, fontStyle = FontStyle.Italic)
            Text("${it.runtime}mins")
            Text(it.overview)
        }
    }

    LaunchedEffect(Unit) {
        // TMDB setup
        movieInfo = TmdbClient.tmdb.movies.getDetails(movieId!!)
        // firestore fetch
        val db = Firebase.firestore
        db.collection("test-max-list")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    println("${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents $exception")
            }
    }
}