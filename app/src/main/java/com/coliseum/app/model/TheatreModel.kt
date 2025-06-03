package com.coliseum.app.model

data class Theatre(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val city: String = "",
    val province: String = "", // Using province instead of state
    val postalCode: String = "", // Using postalCode instead of zipCode
    val phoneNumber: String = "",
    val capacity: Int = 0,
    val totalAuditoriums: Int = 0,
    val auditoriums: List<Auditorium> = listOf(),
    val note: String = "",
    val images: List<String> = listOf(),
)

data class Auditorium(
    val number: Int = 0,
    val type: String = "",
)