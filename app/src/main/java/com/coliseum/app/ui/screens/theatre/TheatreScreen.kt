package com.coliseum.app.ui.screens.theatre

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.coliseum.app.model.TheatreModel
import com.coliseum.app.ui.theme.PurpleGrey40

@Composable
fun TheatreScreen(
    viewModel: TheatreViewModel = viewModel(),
    theatreId: String? = ""
) {
    val theatreInfo by viewModel.theatreInfo.collectAsState()

    theatreInfo?.let {
        if (it.images.isNotEmpty()) {
            AsyncImage(
                model = it.images[0],
                contentDescription = null
            )
        }
        Text(it.name)
        Text("${it.address}, ${it.city}")
        Text("Total Auditoriums: ${it.totalAuditoriums}")

        Spacer(Modifier.height(100.dp))
        AuditoriumsListings(it, {})
    }

    LaunchedEffect(Unit) {
        viewModel.getTheatreInfo(theatreId ?: "")
    }
}

@Composable
fun AuditoriumsListings(
    theatreInfo: TheatreModel,
    onAuditoriumClick: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(theatreInfo.totalAuditoriums) { auditoriumIndex ->
            val auditorium = theatreInfo.auditoriums.find { it.number == (auditoriumIndex + 1) }
            // if found special format
            if (auditorium != null) {
                val formatList = auditorium.type.split("/")
                Column(
                    modifier = Modifier.size(60.dp).background(PurpleGrey40)
                ) {
                    Text(auditorium.number.toString(), color = Color.White)
                    formatList.forEach { Text(it, color = Color.White) }
                }
            }
            // regular theatre
            else {
                Column(
                    modifier = Modifier.size(60.dp).background(Color.Black)
                ) {
                    Text((auditoriumIndex + 1).toString(), color = Color.Gray)
                }
            }
        }
    }
}