package com.coliseum.app.model

data class TheatreModel(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val city: String = "",
    val images: List<String> = listOf<String>(),
    val totalAuditoriums: Int = 0,
    val auditoriums: List<Auditorium> = listOf<Auditorium>()
)

data class Auditorium(
    val number: Int = 0,
    val type: String = "",
)