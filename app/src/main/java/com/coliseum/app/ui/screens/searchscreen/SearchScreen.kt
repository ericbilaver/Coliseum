package com.coliseum.app.ui.screens.searchscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val theatresList = remember { mutableStateListOf<String>() }

    fun callDB() {
        Firebase.firestore
            .collection("theatres-test")
            .whereGreaterThanOrEqualTo("name", searchQuery)
            .whereLessThanOrEqualTo("name", "$searchQuery\uF7FF")
            .get()
            .addOnSuccessListener { documents ->
                theatresList.removeAll(theatresList)
                for (document in documents) {
                    //println("${document.id} => ${document.data}")
                    println(document.data["name"])
                    theatresList.add(document.data["name"].toString())
                }
            }
            .addOnFailureListener { exception ->
                println(exception)
            }
    }


    Text("Search")
    Text("Search")
    SearchBar(
        query = searchQuery,
        onQueryChange = { searchQuery = it },
        onSearch = {


        },
        active = false,
        onActiveChange = {  }
    ) {

    }
    Button(
        onClick = { callDB() }
    ) {
        Text("Search")
    }
    theatresList.forEach {
        Text(it)
    }

}