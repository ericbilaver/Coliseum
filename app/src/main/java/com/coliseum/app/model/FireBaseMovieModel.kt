package com.coliseum.app.model


data class Movie(
    val id: String = "",
    val aspectRatios: List<String> = listOf(),
    val extraformat: String = "",
    val imax: String = "",
    val information: String = "",
    val notes: String = "",
    val title: String = "",
    val tmdbid: Long = 0
)