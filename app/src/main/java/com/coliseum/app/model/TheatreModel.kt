package com.coliseum.app.model

data class TheatreModel(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val city: String = "",
    val images: List<String> = listOf<String>(),
    val totalAuditoriums: Int = 0,


    )