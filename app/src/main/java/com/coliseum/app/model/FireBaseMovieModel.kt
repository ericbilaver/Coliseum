package com.coliseum.app.model

import com.coliseum.app.ui.screens.movie.MovieFormat

data class FireBaseMovieModel(
    val format: MovieFormat? = null,
    val aspectRatios: List<String>? = null,
    val imax: String? = null,
    val notes: String? = null,
)