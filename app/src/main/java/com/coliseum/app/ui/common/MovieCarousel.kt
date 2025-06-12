package com.coliseum.app.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.moviebase.tmdb.model.TmdbMovie
import coil3.compose.AsyncImage

@Composable
fun MovieCarousel(
    items: List<TmdbMovie>,
    onMovieClick: (movieId: Int) -> Unit
) {
    // Display 10 items
    val pagerState = rememberPagerState { items.size }

    Column(
        Modifier
            .height(200.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 0.dp,
            pageSize = PageSize.Fixed(150.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) { page ->
            val movie = items[page]
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500/${movie.posterImage?.path}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable {
                        onMovieClick(movie.id)
                    }
            )
        }

    }
}
