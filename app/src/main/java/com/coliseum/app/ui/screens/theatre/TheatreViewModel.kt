package com.coliseum.app.ui.screens.theatre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coliseum.app.model.Theatre
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class TheatreViewModel @Inject constructor(): ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(TheatreUiState())
    val uiState: StateFlow<TheatreUiState> = _uiState
    val theatreInfo = MutableStateFlow<Theatre?>(null)

    fun fetchTheatre(theatreId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                if (theatreId.trim().isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Theatre ID cannot be empty"
                    )
                    return@launch
                }

                val document = db.collection("theatres-test")
                    .document(theatreId.trim())
                    .get()
                    .await()

                if (document.exists()) {
                    // Theatre exists, load it
                    val theatre = document.toObject(Theatre::class.java)?.copy(id = document.id)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        theatre = theatre,
                        error = null,
                        isCreatingNew = false
                    )
                } else {
                    // Theatre doesn't exist, prepare for creation
                    val newTheatre = Theatre(
                        name = theatreId.trim(),
                        address = "",
                        city = "",
                        province = "",
                        postalCode = "",
                        phoneNumber = "",
                        capacity = 0,
                        totalAuditoriums = 0,
                        auditoriums = listOf(),
                        note = ""
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        theatre = newTheatre,
                        error = null,
                        isCreatingNew = true
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error loading theatre: ${e.message}"
                )
            }
        }
    }

    fun updateTheatre(theatreId: String, theatre: Theatre) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Convert auditoriums to the correct format for Firestore
                val auditoriumsMap = theatre.auditoriums.map {auditorium ->
                    mapOf(
                        "number" to auditorium.number,
                        "type" to auditorium.type
                    )
                }

                val theatreMap = hashMapOf<String, Any>(
                    "name" to theatre.name,
                    "address" to theatre.address,
                    "city" to theatre.city,
                    "province" to theatre.province,
                    "postalCode" to theatre.postalCode,
                    "phoneNumber" to theatre.phoneNumber,
                    "capacity" to theatre.capacity,
                    "totalAuditoriums" to theatre.totalAuditoriums,
                    "auditoriums" to auditoriumsMap,
                    "note" to theatre.note,
                    "images" to theatre.images.map { it }
                )

                db.collection("theatres-test").document(theatreId).update(theatreMap).await()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    theatre = theatre,
                    success = "Theatre updated successfully",
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error updating theatre: ${e.message}"
                )
            }
        }
    }

    fun createTheatre(theatre: Theatre) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Convert auditoriums to the correct format for Firestore

                val theatreMap = hashMapOf<String, Any>(
                    "name" to theatre.name,
                    "address" to theatre.address,
                    "city" to theatre.city,
                    "province" to theatre.province,
                    "postalCode" to theatre.postalCode,
                    "phoneNumber" to theatre.phoneNumber,
                    "capacity" to theatre.capacity,
                    "totalAuditoriums" to theatre.totalAuditoriums,
                    "auditoriums" to theatre.auditoriums,
                    "note" to theatre.note,
                    "images" to theatre.images
                )

                val documentRef = db.collection("theatres-test").add(theatreMap).await()

                val createdTheatre = theatre.copy(id = documentRef.id)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    theatre = createdTheatre,
                    success = "Theatre created successfully",
                    error = null,
                    isCreatingNew = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error creating theatre: ${e.message}"
                )
            }
        }
    }


    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, success = null)
    }
}

data class TheatreUiState(
    val isLoading: Boolean = false,
    val theatre: Theatre? = null,
    val error: String? = null,
    val success: String? = null,
    val isCreatingNew: Boolean = false
)