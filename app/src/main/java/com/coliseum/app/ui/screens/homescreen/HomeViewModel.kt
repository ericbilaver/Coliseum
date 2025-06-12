package com.coliseum.app.ui.screens.homescreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moviebase.tmdb.discover.DiscoverCategory
import app.moviebase.tmdb.model.TmdbDiscover
import app.moviebase.tmdb.model.TmdbDiscoverFilter
import app.moviebase.tmdb.model.TmdbDiscoverMovieSortBy
import app.moviebase.tmdb.model.TmdbDiscoverTimeRange
import app.moviebase.tmdb.model.TmdbMovie
import app.moviebase.tmdb.model.TmdbReleaseType
import app.moviebase.tmdb.model.TmdbTimeWindow
import com.coliseum.app.TmdbClient
import com.coliseum.app.items
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date.from
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    val nowPlayingMovies = MutableStateFlow<List<TmdbMovie>>(emptyList())
    val comingSoonMovies = MutableStateFlow<List<TmdbMovie>>(emptyList())


    fun getNowPlayingMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val today = java.time.LocalDate.now()
            val oneMonthAgo = today.minusMonths(1)

            val discoverParams = TmdbDiscover.Movie(
                sortBy = TmdbDiscoverMovieSortBy.POPULARITY,
                releaseDate = TmdbDiscoverTimeRange.Custom(firstDate = oneMonthAgo.toString(), lastDate = today.toString()),
                withReleaseTypes = TmdbDiscoverFilter(items = listOf(TmdbReleaseType.THEATRICAL))

            )

            nowPlayingMovies.value = TmdbClient.tmdb.discover.discoverMovie(
                page = 1,
                region = "US",
                language = "US",
                discover = discoverParams
            ).results
        }
    }

    fun getComingSoonMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val today = java.time.LocalDate.now()
            val twoMonthFuture = today.plusMonths(2)

            val discoverParams = TmdbDiscover.Movie(
                sortBy = TmdbDiscoverMovieSortBy.POPULARITY,
                releaseDate = TmdbDiscoverTimeRange.Custom(firstDate = today.toString(), lastDate = twoMonthFuture.toString()),
                withReleaseTypes = TmdbDiscoverFilter(items = listOf(TmdbReleaseType.THEATRICAL))
            )

            comingSoonMovies.value = TmdbClient.tmdb.discover.discoverMovie(
                page = 1,
                region = "US",
                language = "US",
                discover = discoverParams
            ).results
        }
    }
}