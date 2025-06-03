package com.coliseum.app

import MovieScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.moviebase.tmdb.Tmdb3
import com.coliseum.app.ui.screens.homescreen.HomeScreen
import com.coliseum.app.ui.screens.searchscreen.SearchScreen
import com.coliseum.app.ui.screens.theatre.TheatreScreen
import com.coliseum.app.ui.screens.user.UserScreen
import com.coliseum.app.ui.theme.ColiseumTheme
import com.coliseum.app.ui.theme.Pink40

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            val rootNavController = rememberNavController()
            val navBackStackEntry = rootNavController.currentBackStackEntryAsState()
            ColiseumTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar (
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = Color.White,

                        ) {
                            items.forEach { item ->
                                val isSelected = item.title.lowercase() == navBackStackEntry.value?.destination?.route
                                NavigationBarItem(
                                    selected = isSelected,
                                    label = {
                                        Text(text = item.title)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if(isSelected) {
                                                item.selectedIcon
                                            } else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    },
                                    onClick = {
                                        rootNavController.navigate(item.title.lowercase()) {
                                            popUpTo(rootNavController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }

                        }
                    }
                ) { _ ->
                    Box(Modifier.fillMaxSize()) {
                        NavHost(
                            navController = rootNavController,
                            startDestination = HomeScreens.Home.route,
                        ) {
                            // Define your navigation routes here
                            composable(Screen.Home.route) {
                                HomeNavHost()
                            }
                            composable(Screen.Search.route) {
                                SearchNavHost()
                            }
                            composable(Screen.User.route) {
                                UserNavHost()
                            }
                        }
                    }
                }
            }
        }
    }
}

// Define the navigation routes as sealed class
sealed class Screen(val route: String) {
    object Home : HomeScreens("home")
    object Search : HomeScreens("search")
    object User : HomeScreens("user")
}


@Composable
fun HomeNavHost() {
    // Define the navigation routes as sealed class
    val homeNavController = rememberNavController()
    NavHost(homeNavController, startDestination = "home") {
        // Define your navigation routes here
        composable(Screen.Home.route) {
            Column {
                Text("Home")
                HomeScreen(
                    onClick = { movieId ->
                        homeNavController.navigate(HomeScreens.MovieDetails.createRoute(movieId))
                    }
                )
            }

        }
        composable(
            HomeScreens.MovieDetails.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) {
            val movieId = it.arguments?.getInt("movieId") ?: 0
            Column {
                Text("Detail")
                Button(onClick = { homeNavController.popBackStack() }) {
                    Text("Go Back")
                }
                MovieScreen(movieId = movieId.toString())
            }
        }
    }
}

@Composable
fun SearchNavHost() {
    val searchNavController = rememberNavController()
    NavHost(searchNavController, startDestination = SearchScreens.Search.route) {
        composable(SearchScreens.Search.route) {
            Column {
                SearchScreen(
                    onTheatreClick = { theatreId ->
                        searchNavController.navigate(SearchScreens.Theatre.createRoute(theatreId))
                    }
                )
            }
        }

        composable(
            SearchScreens.Theatre.route,
            arguments = listOf(
                navArgument("theatreId") { type = NavType.StringType }
            )
        ) {
            val theatreId = it.arguments?.getString("theatreId") ?: ""
            Column(Modifier.padding(20.dp)) {
                Button(onClick = { searchNavController.popBackStack() }) {
                    Text("Go Back")
                }
                TheatreScreen(theatreId = theatreId)
            }
        }
    }
}

@Composable
fun UserNavHost() {
    val userNavController = rememberNavController()
    NavHost(userNavController, startDestination = "user") {
        composable("user") {
            UserScreen()
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

val items = listOf(
    BottomNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BottomNavigationItem(
        title = "Search",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
    ),
    BottomNavigationItem(
        title = "User",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    ),
)

sealed class HomeScreens(val route: String) {
    object Home : HomeScreens("home")
    object MovieDetails : HomeScreens("movie/{movieId}") {
        fun createRoute(movieId: Int): String {
            return "movie/$movieId"
        }
    }
}

sealed class SearchScreens(val route: String) {
    object Search : SearchScreens("home")
    object Theatre : SearchScreens("theatre/{theatreId}") {
        fun createRoute(theatreId: String): String {
            return "theatre/$theatreId"
        }
    }
}


object TmdbClient {
    private const val API_KEY = BuildConfig.TMDB_API_KEY
    val tmdb = Tmdb3(API_KEY)
}