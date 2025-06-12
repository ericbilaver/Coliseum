package com.coliseum.app.ui.screens.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchToggle(
    selected: SearchType = SearchType.THEATRES,
    onSelectedChange: (SearchType) -> Unit
) {
    val items = listOf(SearchType.MOVIES, SearchType.THEATRES)

    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
    ) {
        items.forEach { type ->
            val isSelected = type == selected
            val backgroundColor = if (isSelected) Color.Blue else Color.Transparent
            val contentColor = if (isSelected) Color.White else Color.Black

            TextButton(
                onClick = { onSelectedChange(type) },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(text = type.displayName)
            }
        }
    }
}

enum class SearchType(val displayName: String) {
    MOVIES("Movies"),
    THEATRES("Theatres")
}
