package com.coliseum.app.ui.screens.theatre

import androidx.lifecycle.ViewModel
import com.coliseum.app.model.TheatreModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TheatreViewModel @Inject constructor(): ViewModel() {
    val theatreInfo = MutableStateFlow<TheatreModel?>(null)

    fun getTheatreInfo(theatreId: String) {
        Firebase.firestore.collection("theatres-test")
            .document(theatreId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val theatre = documentSnapshot.toObject(TheatreModel::class.java)
                    theatreInfo.value = theatre
                }
            }
            .addOnFailureListener { exception ->
                println(exception)
            }
    }
}